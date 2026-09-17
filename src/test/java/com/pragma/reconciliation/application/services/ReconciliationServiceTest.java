package com.pragma.reconciliation.application.services;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.domain.model.Reconciliation.MovementMatch;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceTest {

    @Mock
    private PostgreSQLReconciliationRepository repository;

    @Mock
    private ReconciliationEventPublisher eventPublisher;

    private ReconciliationService service;

    @BeforeEach
    void setUp() {
        service = new ReconciliationService(repository, eventPublisher, 5, 40);
    }

    @Nested
    @DisplayName("Procesamiento de movimientos")
    class ProcessMovement {

        @Test
        @DisplayName("Debe crear nueva reconciliación cuando no existe")
        void shouldCreateNewReconciliationWhenNotExists() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertNotNull(reconciliation.getId());
                    assertEquals(Status.PENDING, reconciliation.getStatus());
                    assertEquals(command.sourceReference(), reconciliation.getSourceReference());
                    assertEquals(command.amount(), reconciliation.getAmount());
                })
                .verifyComplete();

            verify(repository).save(any(Reconciliation.class));
            verify(eventPublisher, times(1)).publish(any(ReconciliationEvent.Created.class));
        }

        @Test
        @DisplayName("Debe rechazar movimiento duplicado por idempotencia")
        void shouldRejectDuplicateByIdempotency() {
            String eventId = "evt-" + UUID.randomUUID();
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            Reconciliation existing = new Reconciliation(
                "recon-123", eventId, 1, Status.PENDING,
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD",
                Instant.now(), Instant.now(), Instant.now()
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.just(existing));

            StepVerifier.create(service.processMovement(command))
                .verifyErrorMatches(e -> e.getMessage().contains("Idempotency"));

            verify(repository, never()).save(any());
            verify(eventPublisher, never()).publish(any());
        }

        @Test
        @DisplayName("Debe aceptar reprocesamiento con versión mayor")
        void shouldAcceptReprocessingWithHigherVersion() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            Reconciliation existing = new Reconciliation(
                "recon-123", "evt-old", 1, Status.PENDING,
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD",
                Instant.now(), Instant.now(), Instant.now()
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.just(existing));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertEquals(2, reconciliation.getVersion());
                    assertTrue(reconciliation.isIdempotencyProcessed());
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Gestión de ventana de matching")
    class MatchingWindow {

        @Test
        @DisplayName("Debe ejecutar matching dentro de la ventana")
        void shouldExecuteMatchingWithinWindow() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-002", "CORE_BANK", new BigDecimal("2000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertTrue(reconciliation.isWithinMatchingWindow(Instant.now(), 5));
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe rechazar movimiento fuera de ventana para matching")
        void shouldRejectMovementOutsideMatchingWindow() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-003", "CORE_BANK", new BigDecimal("3000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.findUnmatchedWithinWindow(any(Instant.class), anyLong()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertEquals(Status.PENDING, reconciliation.getStatus());
                    assertFalse(reconciliation.isWithinMatchingWindow(Instant.now(), 5));
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Resolución de discrepancias")
    class DiscrepancyResolution {

        @Test
        @DisplayName("Debe marcar como matched cuando encuentra coincidencias")
        void shouldMarkAsMatchedWhenMatchesFound() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-004", "CORE_BANK", new BigDecimal("4000.00"), "USD"
            );

            List<MovementMatch> matches = List.of(
                new MovementMatch("core-001", "CORE_BANK", new BigDecimal("4000.00")),
                new MovementMatch("gw-001", "PAYMENT_GW", new BigDecimal("4000.00"))
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.Matched.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.resolveReconciliation(reconciliation.getId(), true, matches))
                .assertNext(r -> assertEquals(Status.MATCHED, r.getStatus()))
                .verifyComplete();

            verify(eventPublisher).publish(any(ReconciliationEvent.Matched.class));
        }

        @Test
        @DisplayName("Debe marcar como mismatched cuando no hay coincidencias")
        void shouldMarkAsMismatchedWhenNoMatches() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-005", "CORE_BANK", new BigDecimal("5000.00"), "USD"
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.Mismatched.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.resolveReconciliation(reconciliation.getId(), false, List.of()))
                .assertNext(r -> {
                    assertEquals(Status.MISMATCHED, r.getStatus());
                    assertNotNull(r.getMismatchReason());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe escalar a revisión manual cuando supera SLA")
        void shouldEscalateToManualReviewWhenSlaExceeded() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-006", "CORE_BANK", new BigDecimal("6000.00"), "USD"
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.ManualReviewEscalation.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.checkAndEscalateIfNeeded(reconciliation.getId()))
                .assertNext(r -> {
                    assertTrue(r.requiresManualIntervention(40, Instant.now()));
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Consultas de estado")
    class StatusQueries {

        @Test
        @DisplayName("Debe retornar estado de reconciliación por ID")
        void shouldReturnStatusById() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-007", "CORE_BANK", new BigDecimal("7000.00"), "USD"
            );
            reconciliation.markAsMatched(List.of(
                new MovementMatch("m1", "CORE_BANK", new BigDecimal("7000.00"))
            ));

            when(repository.findById("TXN-007")).thenReturn(Mono.just(reconciliation));

            StepVerifier.create(service.getReconciliationStatus("TXN-007"))
                .assertNext(status -> {
                    assertEquals(Status.MATCHED, status.getStatus());
                    assertNotNull(status.getMatchedAt());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe retornar vacío cuando reconciliación no existe")
        void shouldReturnEmptyWhenNotFound() {
            when(repository.findById(anyString())).thenReturn(Mono.empty());

            StepVerifier.create(service.getReconciliationStatus("NON-EXISTENT"))
                .verifyComplete();
        }
    }
}