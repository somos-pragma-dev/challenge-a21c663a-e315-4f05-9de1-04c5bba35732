package com.pragma.reconciliation.domain.events;

import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface ReconciliationEvent permits
        ReconciliationEvent.Created,
        ReconciliationEvent.Matched,
        ReconciliationEvent.Mismatched,
        ReconciliationEvent.ManualReviewEscalation,
        ReconciliationEvent.ManualReviewResolved,
        ReconciliationEvent.IdempotentProcessed {

    String eventId();
    String reconciliationId();
    Instant occurredAt();

    record Created(
        String eventId,
        String reconciliationId,
        String sourceReference,
        String sourceSystem,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Created create(String reconciliationId, String sourceReference,
                                     String sourceSystem, BigDecimal amount, String currency,
                                     Instant transactionDate) {
            return new Created(
                UUID.randomUUID().toString(),
                reconciliationId,
                sourceReference,
                sourceSystem,
                amount,
                currency,
                transactionDate,
                Instant.now()
            );
        }
    }

    record Matched(
        String eventId,
        String reconciliationId,
        List<MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Matched create(String reconciliationId,
                                     List<MovementMatch> matchedMovements) {
            return new Matched(
                UUID.randomUUID().toString(),
                reconciliationId,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record Mismatched(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Mismatched create(String reconciliationId, String reason) {
            return new Mismatched(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewEscalation(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewEscalation create(String reconciliationId, String reason) {
            return new ManualReviewEscalation(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewResolved(
        String eventId,
        String reconciliationId,
        boolean matched,
        List<MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewResolved create(String reconciliationId, boolean matched,
                                                   List<MovementMatch> matchedMovements) {
            return new ManualReviewResolved(
                UUID.randomUUID().toString(),
                reconciliationId,
                matched,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record IdempotentProcessed(
        String eventId,
        String reconciliationId,
        String originalEventId,
        int version,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static IdempotentProcessed create(String reconciliationId, String originalEventId, int version) {
            return new IdempotentProcessed(
                UUID.randomUUID().toString(),
                reconciliationId,
                originalEventId,
                version,
                Instant.now()
            );
        }
    }
}