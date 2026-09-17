package com.pragma.reconciliation.application.services;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.domain.queries.GetReconciliationStatusQuery;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación que coordina los casos de uso de conciliación,
 * incluyendo el procesamiento de movimientos y la gestión de ventanas de matching.
 * Implementa la lógica de orchestación entre el dominio y la infraestructura.
 */
@Service
public class ReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);

    private final PostgreSQLReconciliationRepository reconciliationRepository;
    private final ReconciliationEventPublisher eventPublisher;
    private final ReconciliationConfig config;

    public ReconciliationService(
            PostgreSQLReconciliationRepository reconciliationRepository,
            ReconciliationEventPublisher eventPublisher,
            ReconciliationConfig config) {
        this.reconciliationRepository = reconciliationRepository;
        this.eventPublisher = eventPublisher;
        this.config = config;
    }

    public Mono<Reconciliation> processMovement(ProcessMovementCommand command) {
        log.info("Procesando movimiento: referencia={}, sistema={}, monto={}",
            command.sourceReference(), command.sourceSystem(), command.amount());

        if (!command.isValidForMatchingWindow(config.getMatchingWindowMinutes())) {
            log.warn("Movimiento fuera de la ventana de matching: {}", command.getCompositeKey());
            return Mono.error(new IllegalArgumentException(
                "El movimiento está fuera de la ventana de matching permitida"));
        }

        return reconciliationRepository.findBySourceReferenceAndSystem(
                command.sourceReference(), command.sourceSystem())
            .flatMap(existingReconciliation -> handleExistingReconciliation(existingReconciliation, command))
            .switchIfEmpty(Mono.defer(() -> createNewReconciliation(command)))
            .flatMap(reconciliation -> reconciliationRepository.save(reconciliation))
            .flatMap(savedReconciliation -> publishReconciliationEvent(savedReconciliation)
                .thenReturn(savedReconciliation))
            .doOnSuccess(reconciliation -> log.info("Movimiento procesado exitosamente: {}", reconciliation.getId()))
            .doOnError(error -> log.error("Error al procesar movimiento: {}", error.getMessage()));
    }

    private Mono<Reconciliation> handleExistingReconciliation(
            Reconciliation existing, ProcessMovementCommand command) {
        
        log.debug("Conciliación existente encontrada: {}, version={}, eventId={}",
            existing.getId(), existing.getVersion(), existing.getEventId());

        if (existing.isIdempotentWith(command.eventId())) {
            log.info("Procesamiento idempotente detectado para conciliación: {}", existing.getId());
            return Mono.just(existing.applyIdempotent(command.eventId(), command.version()));
        }

        if (existing.isDuplicateVersion(command.version())) {
            log.warn("Versión duplicada detectada: {} vs {}", existing.getVersion(), command.version());
            return Mono.error(new IllegalStateException(
                "Versión duplicada recibida para la misma conciliación"));
        }

        return performMatching(existing, command);
    }

    private Mono<Reconciliation> createNewReconciliation(ProcessMovementCommand command) {
        log.debug("Creando nueva conciliación para movimiento: {}", command.getCompositeKey());
        
        String reconciliationId = UUID.randomUUID().toString();
        String eventId = command.eventId();
        int version = 1;

        Reconciliation reconciliation = new Reconciliation(
            reconciliationId,
            eventId,
            version,
            Status.PENDING,
            command.sourceReference(),
            command.sourceSystem(),
            command.amount(),
            command.currency(),
            command.transactionDate(),
            Instant.now(),
            Instant.now()
        );

        return performMatching(reconciliation, command);
    }

    private Mono<Reconciliation> performMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        log.debug("Ejecutandomatching para conciliación: {}", reconciliation.getId());

        List<MovementMatch> matches = findMatchingMovements(reconciliation, command);
        
        if (!matches.isEmpty()) {
            reconciliation.markAsMatched(matches);
            log.info("Conciliación {} marcada comoMatched con {} movimientos",
                reconciliation.getId(), matches.size());
        } else if (reconciliation.requiresManualIntervention(config.getSlaMinutes(), Instant.now())) {
            reconciliation.escalateToManualReview();
            log.warn("Conciliación {} escalada a revisión manual por SLA", reconciliation.getId());
        } else {
            reconciliation.markAsMismatched("No se encontraron movimientos coincidentes en las fuentes");
            log.info("Conciliación {} marcada como Mismatched", reconciliation.getId());
        }

        return Mono.just(reconciliation);
    }

    private List<MovementMatch> findMatchingMovements(Reconciliation reconciliation, ProcessMovementCommand command) {
        BigDecimal tolerance = config.getAmountTolerance();
        BigDecimal amount = command.amount();
        
        return List.of(
            new MovementMatch(command.sourceSystem(), command.sourceReference(), amount)
        );
    }

    public Mono<Reconciliation> getReconciliationStatus(GetReconciliationStatusQuery query) {
        log.debug("Consultando estado de conciliación: {}", query.getReconciliationId());
        
        return reconciliationRepository.findById(query.getReconciliationId())
            .doOnSuccess(reconciliation -> {
                if (reconciliation != null) {
                    log.debug("Estado de conciliación {}: {}", 
                        query.getReconciliationId(), reconciliation.getStatus());
                }
            })
            .doOnError(error -> log.error("Error al consultar conciliación: {}", error.getMessage()));
    }

    public Mono<Boolean> resolveManualReview(String reconciliationId, boolean matched, List<MovementMatch> matches) {
        log.info("Resolviendo revisión manual para conciliación: {}, matched={}", reconciliationId, matched);
        
        return reconciliationRepository.findById(reconciliationId)
            .flatMap(reconciliation -> {
                reconciliation.resolveFromManualReview(matched, matches);
                return reconciliationRepository.save(reconciliation)
                    .flatMap(saved -> publishReconciliationEvent(saved).thenReturn(true));
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "Conciliación no encontrada: " + reconciliationId)));
    }

    public Flux<Reconciliation> getReconciliationsByStatus(Status status) {
        log.debug("Consultando conciliaciones con estado: {}", status);
        return reconciliationRepository.findByStatus(status);
    }

    public Mono<Long> countReconciliationsByStatus(Status status) {
        return reconciliationRepository.countByStatus(status);
    }

    private Mono<Void> publishReconciliationEvent(Reconciliation reconciliation) {
        ReconciliationEvent event;
        
        switch (reconciliation.getStatus()) {
            case MATCHED -> {
                event = new Matched(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMatchedMovements()
                );
            }
            case MISMATCHED -> {
                event = new Mismatched(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMismatchReason()
                );
            }
            case MANUAL_REVIEW -> {
                event = new ManualReviewEscalation(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMismatchReason()
                );
            }
            default -> {
                return Mono.empty();
            }
        }

        return eventPublisher.publish(event)
            .doOnSuccess(v -> log.debug("Evento publicado: {}", event.getClass().getSimpleName()))
            .doOnError(error -> log.error("Error al publicar evento: {}", error.getMessage()));
    }
}