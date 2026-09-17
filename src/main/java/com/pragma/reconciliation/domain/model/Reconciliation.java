package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Reconciliation {

    public enum Status {
        PENDING,
        MATCHED,
        MISMATCHED,
        MANUAL_REVIEW;

        public boolean canTransitionTo(Status target) {
            return switch (this) {
                case PENDING -> target == MATCHED || target == MISMATCHED || target == MANUAL_REVIEW;
                case MATCHED -> false;
                case MISMATCHED -> target == MANUAL_REVIEW || target == MATCHED;
                case MANUAL_REVIEW -> target == MATCHED || target == MISMATCHED;
            };
        }
    }

    private final String id;
    private final String eventId;
    private final int version;
    private Status status;
    private final String sourceReference;
    private final String sourceSystem;
    private final BigDecimal amount;
    private final String currency;
    private final Instant transactionDate;
    private final Instant createdAt;
    private Instant updatedAt;
    private List<MovementMatch> matchedMovements;
    private String mismatchReason;
    private boolean idempotencyProcessed;

    public Reconciliation(String sourceReference, String sourceSystem, BigDecimal amount,
                          String currency, Instant transactionDate) {
        this.id = UUID.randomUUID().toString();
        this.eventId = UUID.randomUUID().toString();
        this.version = 1;
        this.status = Status.PENDING;
        this.sourceReference = Objects.requireNonNull(sourceReference, "sourceReference no puede ser null");
        this.sourceSystem = Objects.requireNonNull(sourceSystem, "sourceSystem no puede ser null");
        this.amount = Objects.requireNonNull(amount, "amount no puede ser null");
        this.currency = Objects.requireNonNull(currency, "currency no puede ser null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "transactionDate no puede ser null");
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.matchedMovements = List.of();
        this.idempotencyProcessed = false;
    }

    public Reconciliation(String id, String eventId, int version, Status status,
                          String sourceReference, String sourceSystem, BigDecimal amount,
                          String currency, Instant transactionDate, Instant createdAt,
                          Instant updatedAt, List<MovementMatch> matchedMovements,
                          String mismatchReason, boolean idempotencyProcessed) {
        this.id = id;
        this.eventId = eventId;
        this.version = version;
        this.status = status;
        this.sourceReference = sourceReference;
        this.sourceSystem = sourceSystem;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.matchedMovements = matchedMovements != null ? matchedMovements : List.of();
        this.mismatchReason = mismatchReason;
        this.idempotencyProcessed = idempotencyProcessed;
    }

    public boolean isIdempotentWith(String incomingEventId) {
        if (incomingEventId == null) {
            return false;
        }
        if (this.eventId.equals(incomingEventId)) {
            return true;
        }
        return false;
    }

    public boolean isDuplicateVersion(int incomingVersion) {
        return incomingVersion <= this.version;
    }

    public Reconciliation applyIdempotent(String incomingEventId, int incomingVersion) {
        if (isIdempotentWith(incomingEventId) && isDuplicateVersion(incomingVersion)) {
            this.idempotencyProcessed = true;
            return this;
        }
        throw new IllegalArgumentException("Evento no idempotente: eventId=" + incomingEventId
                + ", incomingVersion=" + incomingVersion + ", currentVersion=" + this.version);
    }

    public boolean transitionTo(Status newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            return false;
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
        this.version++;
        return true;
    }

    public void markAsMatched(List<MovementMatch> matches) {
        if (!transitionTo(Status.MATCHED)) {
            throw new IllegalStateException("Transición a MATCHED no permitida desde " + this.status);
        }
        this.matchedMovements = List.copyOf(matches);
        this.mismatchReason = null;
    }

    public void markAsMismatched(String reason) {
        if (!transitionTo(Status.MISMATCHED)) {
            throw new IllegalStateException("Transición a MISMATCHED no permitida desde " + this.status);
        }
        this.mismatchReason = Objects.requireNonNull(reason, "mismatchReason no puede ser null");
    }

    public void escalateToManualReview() {
        if (!transitionTo(Status.MANUAL_REVIEW)) {
            throw new IllegalStateException("Transición a MANUAL_REVIEW no permitida desde " + this.status);
        }
    }

    public void resolveFromManualReview(boolean matched, List<MovementMatch> matches) {
        if (this.status != Status.MANUAL_REVIEW) {
            throw new IllegalStateException("Solo se puede resolver desde MANUAL_REVIEW");
        }
        if (matched) {
            this.status = Status.MATCHED;
            this.matchedMovements = List.copyOf(matches);
            this.mismatchReason = null;
        } else {
            this.status = Status.MISMATCHED;
        }
        this.updatedAt = Instant.now();
        this.version++;
    }

    public boolean isWithinMatchingWindow(Instant now, long windowMinutes) {
        return this.transactionDate.plusSeconds(windowMinutes * 60).isAfter(now);
    }

    public boolean requiresManualIntervention(long slaMinutes, Instant now) {
        return this.status == Status.PENDING
                && this.createdAt.plusSeconds(slaMinutes * 60).isBefore(now);
    }

    public String getId() { return id; }
    public String getEventId() { return eventId; }
    public int getVersion() { return version; }
    public Status getStatus() { return status; }
    public String getSourceReference() { return sourceReference; }
    public String getSourceSystem() { return sourceSystem; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Instant getTransactionDate() { return transactionDate; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<MovementMatch> getMatchedMovements() { return matchedMovements; }
    public String getMismatchReason() { return mismatchReason; }
    public boolean isIdempotencyProcessed() { return idempotencyProcessed; }

    public record MovementMatch(
        String sourceReference,
        String sourceSystem,
        BigDecimal amount,
        Instant matchedAt
    ) {}
}