package com.pragma.reconciliation.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReconciliationTest {

    private static final String SOURCE_REF = "TXN-2024-001";
    private static final String SOURCE_SYSTEM = "CORE_BANK";
    private static final BigDecimal AMOUNT = new BigDecimal("1500.50");
    private static final String CURRENCY = "USD";

    @Nested
    @DisplayName("Creación de instancia")
    class Creation {

        @Test
        @DisplayName("Debe crear instancia con estado PENDING por defecto")
        void shouldCreateWithPendingStatus() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            assertNotNull(reconciliation.getId());
            assertNotNull(reconciliation.getEventId());
            assertEquals(0, reconciliation.getVersion());
            assertEquals(Reconciliation.Status.PENDING, reconciliation.getStatus());
            assertEquals(SOURCE_REF, reconciliation.getSourceReference());
            assertEquals(SOURCE_SYSTEM, reconciliation.getSourceSystem());
            assertEquals(AMOUNT, reconciliation.getAmount());
            assertEquals(CURRENCY, reconciliation.getCurrency());
            assertNotNull(reconciliation.getCreatedAt());
            assertFalse(reconciliation.isIdempotencyProcessed());
        }

        @Test
        @DisplayName("Debe crear instancia con parámetros completos")
        void shouldCreateWithFullParameters() {
            String id = "recon-123";
            String eventId = "evt-456";
            int version = 5;
            Reconciliation.Status status = Reconciliation.Status.MATCHED;
            Instant transactionDate = Instant.now().minus(1, ChronoUnit.HOURS);

            Reconciliation reconciliation = new Reconciliation(
                id, eventId, version, status,
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY,
                transactionDate, Instant.now(), Instant.now()
            );

            assertEquals(id, reconciliation.getId());
            assertEquals(eventId, reconciliation.getEventId());
            assertEquals(version, reconciliation.getVersion());
            assertEquals(status, reconciliation.getStatus());
            assertEquals(transactionDate, reconciliation.getTransactionDate());
        }
    }

    @Nested
    @DisplayName("Transiciones de estado")
    class StateTransitions {

        private Reconciliation reconciliation;

        @BeforeEach
        void setUp() {
            reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
        }

        @ParameterizedTest
        @EnumSource(
            value = Reconciliation.Status.class,
            names = {"MATCHED", "MISMATCHED"}
        )
        @DisplayName("Debe permitir transición desde PENDING a estados terminales")
        void shouldTransitionFromPendingToTerminal(Reconciliation.Status targetStatus) {
            boolean result = reconciliation.transitionTo(targetStatus);

            assertTrue(result);
            assertEquals(targetStatus, reconciliation.getStatus());
        }

        @Test
        @DisplayName("No debe permitir transición desde MATCHED a PENDING")
        void shouldNotAllowTransitionFromMatchedToPending() {
            reconciliation.transitionTo(Reconciliation.Status.MATCHED);
            boolean result = reconciliation.transitionTo(Reconciliation.Status.PENDING);

            assertFalse(result);
            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
        }

        @Test
        @DisplayName("No debe permitir transición desde MISMATCHED a PENDING")
        void shouldNotAllowTransitionFromMismatchedToPending() {
            reconciliation.transitionTo(Reconciliation.Status.MISMATCHED);
            boolean result = reconciliation.transitionTo(Reconciliation.Status.PENDING);

            assertFalse(result);
            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
        }

        @Test
        @DisplayName("Debe permitir transición a MANUAL_REVIEW desde cualquier estado no terminal")
        void shouldAllowTransitionToManualReview() {
            boolean result = reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);

            assertTrue(result);
            assertEquals(Reconciliation.Status.MANUAL_REVIEW, reconciliation.getStatus());
        }

        @Test
        @DisplayName("Debe resolver desde MANUAL_REVIEW con resultado MATCHED")
        void shouldResolveFromManualReviewMatched() {
            reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);
            List<Reconciliation.MovementMatch> matches = List.of(
                new Reconciliation.MovementMatch("src-1", "CORE_BANK", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("src-2", "PAYMENT_GW", new BigDecimal("1500.50"))
            );

            reconciliation.resolveFromManualReview(true, matches);

            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
            assertEquals(matches, reconciliation.getMatchedMovements());
        }

        @Test
        @DisplayName("Debe resolver desde MANUAL_REVIEW con resultado MISMATCHED")
        void shouldResolveFromManualReviewMismatched() {
            reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);

            reconciliation.resolveFromManualReview(false, List.of());

            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
            assertNotNull(reconciliation.getMismatchReason());
        }
    }

    @Nested
    @DisplayName("Manejo de idempotencia")
    class IdempotencyHandling {

        private Reconciliation reconciliation;

        @BeforeEach
        void setUp() {
            reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
        }

        @Test
        @DisplayName("Debe detectar evento duplicado con mismo eventId")
        void shouldDetectDuplicateEventId() {
            String eventId = "evt-001";
            reconciliation = new Reconciliation(
                reconciliation.getId(), eventId, 0,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isIdempotent = reconciliation.isIdempotentWith(eventId);

            assertTrue(isIdempotent);
        }

        @Test
        @DisplayName("Debe rechazar evento con eventId diferente")
        void shouldRejectDifferentEventId() {
            String existingEventId = "evt-001";
            reconciliation = new Reconciliation(
                reconciliation.getId(), existingEventId, 0,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isIdempotent = reconciliation.isIdempotentWith("evt-002");

            assertFalse(isIdempotent);
        }

        @Test
        @DisplayName("Debe detectar versión duplicada")
        void shouldDetectDuplicateVersion() {
            reconciliation = new Reconciliation(
                reconciliation.getId(), "evt-001", 3,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isDuplicate = reconciliation.isDuplicateVersion(3);

            assertTrue(isDuplicate);
        }

        @Test
        @DisplayName("Debe aceptar versión mayor como reprocesamiento válido")
        void shouldAcceptHigherVersionAsReprocessing() {
            reconciliation = new Reconciliation(
                reconciliation.getId(), "evt-001", 3,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isDuplicate = reconciliation.isDuplicateVersion(5);

            assertFalse(isDuplicate);
        }

        @Test
        @DisplayName("Debe aplicar procesamiento idempotente correctamente")
        void shouldApplyIdempotentProcessing() {
            String incomingEventId = "evt-002";
            int incomingVersion = 2;

            Reconciliation result = reconciliation.applyIdempotent(incomingEventId, incomingVersion);

            assertEquals(incomingEventId, result.getEventId());
            assertEquals(incomingVersion, result.getVersion());
            assertTrue(result.isIdempotencyProcessed());
        }
    }

    @Nested
    @DisplayName("Matching y verificación de ventana")
    class MatchingAndWindow {

        @Test
        @DisplayName("Debe marcar comoMatched con lista de movimientos")
        void shouldMarkAsMatched() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            List<Reconciliation.MovementMatch> matches = List.of(
                new Reconciliation.MovementMatch("core-001", "CORE_BANK", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("gw-001", "PAYMENT_GW", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("liq-001", "LIQUIDATION", new BigDecimal("1500.50"))
            );

            reconciliation.markAsMatched(matches);

            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
            assertEquals(matches, reconciliation.getMatchedMovements());
            assertNull(reconciliation.getMismatchReason());
        }

        @Test
        @DisplayName("Debe marcar comoMismatched con razón")
        void shouldMarkAsMismatched() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
            String reason = "Monto discrepancy: 1500.50 vs 1400.00";

            reconciliation.markAsMismatched(reason);

            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
            assertEquals(reason, reconciliation.getMismatchReason());
        }

        @Test
        @DisplayName("Debe verificar que está dentro de la ventana de matching")
        void shouldBeWithinMatchingWindow() {
            Instant transactionDate = Instant.now().minus(2, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean withinWindow = reconciliation.isWithinMatchingWindow(Instant.now(), 5);

            assertTrue(withinWindow);
        }

        @Test
        @DisplayName("Debe detectar que está fuera de la ventana de matching")
        void shouldBeOutsideMatchingWindow() {
            Instant transactionDate = Instant.now().minus(10, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean withinWindow = reconciliation.isWithinMatchingWindow(Instant.now(), 5);

            assertFalse(withinWindow);
        }

        @ParameterizedTest
        @CsvSource({
            "30, true",
            "45, true",
            "50, false"
        })
        @DisplayName("Debe verificar requiere intervención manual según SLA")
        void shouldRequireManualIntervention(long minutesAgo, boolean expected) {
            Instant transactionDate = Instant.now().minus(minutesAgo, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean requires = reconciliation.requiresManualIntervention(40, Instant.now());

            assertEquals(expected, requires);
        }

        @Test
        @DisplayName("Debe escalar a revisión manual")
        void shouldEscalateToManualReview() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            reconciliation.escalateToManualReview();

            assertEquals(Reconciliation.Status.MANUAL_REVIEW, reconciliation.getStatus());
        }
    }
}