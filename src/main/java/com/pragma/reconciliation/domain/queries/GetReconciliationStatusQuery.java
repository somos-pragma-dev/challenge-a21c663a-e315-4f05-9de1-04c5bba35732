package com.pragma.reconciliation.domain.queries;

import java.time.Instant;

/**
 * Query de CQRS para obtener el estado actual de una conciliación por su identificador.
 * Representa una operación de lectura que no modifica el estado del sistema.
 */
public record GetReconciliationStatusQuery(
    String reconciliationId,
    Instant requestedAt
) {
    public GetReconciliationStatusQuery {
        if (reconciliationId == null || reconciliationId.isBlank()) {
            throw new IllegalArgumentException("El identificador de conciliación no puede ser nulo o vacío");
        }
        if (requestedAt == null) {
            throw new IllegalArgumentException("La marca de tiempo de la solicitud no puede ser nula");
        }
    }

    public static GetReconciliationStatusQuery create(String reconciliationId) {
        return new GetReconciliationStatusQuery(reconciliationId, Instant.now());
    }

    public String getReconciliationId() {
        return reconciliationId;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }
}