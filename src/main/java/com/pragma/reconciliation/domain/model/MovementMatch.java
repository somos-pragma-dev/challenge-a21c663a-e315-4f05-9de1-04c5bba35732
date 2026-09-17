package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;

public record MovementMatch(
    String source,
    String reference,
    BigDecimal amount,
    String matchedWith
) {
    public static MovementMatch create(String source, String reference, BigDecimal amount) {
        return new MovementMatch(source, reference, amount, null);
    }

    public static MovementMatch createWithMatch(String source, String reference, BigDecimal amount, String matchedWith) {
        return new MovementMatch(source, reference, amount, matchedWith);
    }
}