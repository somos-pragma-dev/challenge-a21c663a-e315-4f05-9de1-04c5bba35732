package com.pragma.reconciliation.domain.commands;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Comando que representa un movimiento financiero a procesar para conciliación.
 * Es un record inmutable que contiene toda la información del movimiento.
 */
public record ProcessMovementCommand(
    String eventId,
    String sourceReference,
    String sourceSystem,
    BigDecimal amount,
    String currency,
    Instant transactionDate,
    int version
) {
    
    public static ProcessMovementCommand create(
            String sourceReference,
            String sourceSystem,
            BigDecimal amount,
            String currency,
            Instant transactionDate) {
        
        Objects.requireNonNull(sourceReference, "sourceReference cannot be null");
        Objects.requireNonNull(sourceSystem, "sourceSystem cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(currency, "currency cannot be null");
        Objects.requireNonNull(transactionDate, "transactionDate cannot be null");
        
        return new ProcessMovementCommand(
            java.util.UUID.randomUUID().toString(),
            sourceReference,
            sourceSystem,
            amount,
            currency,
            transactionDate,
            1
        );
    }
    
    public boolean isValidForMatchingWindow(long windowMinutes) {
        Instant now = Instant.now();
        Instant windowStart = now.minus(windowMinutes, ChronoUnit.MINUTES);
        return transactionDate.isAfter(windowStart) || transactionDate.equals(windowStart);
    }
    
    public boolean isDuplicateOf(ProcessMovementCommand other) {
        if (other == null) return false;
        return Objects.equals(sourceReference, other.sourceReference) 
            && Objects.equals(sourceSystem, other.sourceSystem);
    }
    
    public String getCompositeKey() {
        return sourceSystem + "|" + sourceReference;
    }
    
    public record MovementSource(
        String system,
        String reference
    ) {}
}