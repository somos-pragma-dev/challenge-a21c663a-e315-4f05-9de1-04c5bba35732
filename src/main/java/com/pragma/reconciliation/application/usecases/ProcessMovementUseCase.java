package com.pragma.reconciliation.application.usecases;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Created;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.IdempotentProcessed;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewResolved;
import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessMovementUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessMovementUseCase.class);

    private final PostgreSQLReconciliationRepository reconciliationRepository;
    private final ReconciliationEventPublisher eventPublisher;
    private final ReconciliationConfig reconciliationConfig;

    public ProcessMovementUseCase(
            PostgreSQLReconciliationRepository reconciliationRepository,
            ReconciliationEventPublisher eventPublisher,
            ReconciliationConfig reconciliationConfig) {
        this.reconciliationRepository = reconciliationRepository;
        this.eventPublisher = eventPublisher;
        this.reconciliationConfig = reconciliationConfig;
    }

    public Mono<Reconciliation> execute(ProcessMovementCommand command) {
        log.info("Processing movement: sourceRef={}, sourceSystem={}, eventId={}",
                command.sourceReference(), command.sourceSystem(), command.eventId());

        if (!command.isValidForMatchingWindow(reconciliationConfig.getMatching().getWindowMinutes())) {
            log.warn("Movement {} is outside matching window, escalating to manual review",
                    command.eventId());
            return handleOutsideWindow(command);
        }

        return reconciliationRepository.findBySourceReferenceAndSourceSystem(
                        command.sourceReference(), command.sourceSystem())
                .flatMap(existing -> processExistingReconciliation(existing, command))
                .switchIfEmpty(Mono.defer(() -> createNewReconciliation(command)));
    }

    private Mono<Reconciliation> processExistingReconciliation(Reconciliation existing, ProcessMovementCommand command) {
        if (existing.isIdempotentWith(command.eventId())) {
            log.info("Idempotent processing: event {} already handled for reconciliation {}",
                    command.eventId(), existing.getId());
            return reconciliationRepository.save(existing)
                    .flatMap(saved -> publishEventAndReturn(
                            IdempotentProcessed.create(
                                    saved.getId(),
                                    command.eventId(),
                                    saved.getVersion()),
                            saved));
        }

        if (existing.isDuplicateVersion(command.version())) {
            log.warn("Duplicate version {} for reconciliation {}, skipping",
                    command.version(), existing.getId());
            return Mono.just(existing);
        }

        if (existing.getStatus() == Status.MANUAL_REVIEW) {
            return handleManualReviewReception(existing, command);
        }

        return attemptMatching(existing, command);
    }

    private Mono<Reconciliation> attemptMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        Instant now = Instant.now();
        List<MovementMatch> matches = performMatching(reconciliation, command);

        if (!matches.isEmpty() && matchesComplete(reconciliation, matches)) {
            reconciliation.markAsMatched(matches);
            log.info("Reconciliation {} matched with {} movements",
                    reconciliation.getId(), matches.size());
        } else {
            reconciliation.markAsMismatched("No complete match found across all source systems");
            log.warn("Reconciliation {} marked as mismatched", reconciliation.getId());
        }

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> {
                    ReconciliationEvent event = saved.getStatus() == Status.MATCHED
                            ? Matched.create(saved.getId(), saved.getMatchedMovements())
                            : Mismatched.create(saved.getId(), saved.getMismatchReason());
                    return publishEventAndReturn(event, saved);
                });
    }

    private List<MovementMatch> performMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        List<MovementMatch> matches = new ArrayList<>();
        matches.add(MovementMatch.create(
                command.sourceSystem(),
                command.sourceReference(),
                command.amount()));
        return matches;
    }

    private boolean matchesComplete(Reconciliation reconciliation, List<MovementMatch> matches) {
        return matches.size() >= 3;
    }

    private Mono<Reconciliation> handleManualReviewReception(Reconciliation reconciliation, ProcessMovementCommand command) {
        List<MovementMatch> matches = performMatching(reconciliation, command);
        boolean resolved = matchesComplete(reconciliation, matches);

        reconciliation.resolveFromManualReview(resolved, matches);
        log.info("Manual review reconciliation {} resolved: matched={}", reconciliation.getId(), resolved);

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        ManualReviewResolved.create(
                                saved.getId(),
                                resolved,
                                saved.getMatchedMovements()),
                        saved));
    }

    private Mono<Reconciliation> handleOutsideWindow(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.escalateToManualReview();
        log.warn("Movement {} escalated to manual review: outside matching window", command.eventId());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        ManualReviewEscalation.create(
                                saved.getId(),
                                "Outside matching window"),
                        saved));
    }

    private Mono<Reconciliation> createNewReconciliation(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.applyIdempotent(command.eventId(), command.version());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        Created.create(
                                saved.getId(),
                                saved.getSourceReference(),
                                saved.getSourceSystem(),
                                saved.getAmount(),
                                saved.getCurrency(),
                                saved.getTransactionDate()),
                        saved));
    }

    private Mono<Reconciliation> publishEventAndReturn(ReconciliationEvent event, Reconciliation reconciliation) {
        return eventPublisher.publish(event)
                .then(Mono.just(reconciliation))
                .onErrorResume(e -> {
                    log.error("Failed to publish event for reconciliation {}: {}",
                            reconciliation.getId(), e.getMessage());
                    return Mono.just(reconciliation);
                });
    }

    public Mono<Reconciliation> processWithSlaCheck(ProcessMovementCommand command) {
        return execute(command)
                .flatMap(reconciliation -> {
                    if (reconciliation.requiresManualIntervention(
                            reconciliationConfig.getSla().getMinutes(), Instant.now())) {
                        log.warn("Reconciliation {} requires manual intervention - SLA breached",
                                reconciliation.getId());
                        return eventPublisher.publish(ManualReviewEscalation.create(
                                reconciliation.getId(),
                                "SLA breach")).thenReturn(reconciliation);
                    }
                    return Mono.just(reconciliation);
                });
    }
}