package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public class PostgreSQLReconciliationRepository {

    private static final Logger log = LoggerFactory.getLogger(PostgreSQLReconciliationRepository.class);

    private final DatabaseClient databaseClient;

    public PostgreSQLReconciliationRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        initializeSchema();
    }

    private void initializeSchema() {
        databaseClient.sql("""
            CREATE TABLE IF NOT EXISTS reconciliations (
                id VARCHAR(255) PRIMARY KEY,
                event_id VARCHAR(255) NOT NULL,
                version INTEGER NOT NULL DEFAULT 1,
                status VARCHAR(50) NOT NULL,
                source_reference VARCHAR(255) NOT NULL,
                source_system VARCHAR(100) NOT NULL,
                amount DECIMAL(19,4) NOT NULL,
                currency VARCHAR(3) NOT NULL,
                transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                matched_movements JSONB,
                mismatch_reason TEXT,
                idempotency_processed BOOLEAN NOT NULL DEFAULT FALSE
            )
            """)
            .fetch()
            .rowsUpdated()
            .subscribe(
                rows -> log.debug("Esquema de reconciliations inicializado: {} filas afectadas", rows),
                error -> log.error("Error al inicializar esquema de reconciliations", error)
            );
    }

    public Mono<Reconciliation> save(Reconciliation reconciliation) {
        log.info("Persistiendo conciliación con ID: {} y estado: {}", reconciliation.getId(), reconciliation.getStatus());
        
        String matchedMovementsJson = serializeMatchedMovements(reconciliation.getMatchedMovements());
        
        return databaseClient.sql("""
            INSERT INTO reconciliations 
                (id, event_id, version, status, source_reference, source_system, 
                 amount, currency, transaction_date, created_at, updated_at, 
                 matched_movements, mismatch_reason, idempotency_processed)
            VALUES 
                (:id, :eventId, :version, :status, :sourceReference, :sourceSystem,
                 :amount, :currency, :transactionDate, :createdAt, :updatedAt,
                 :matchedMovements, :mismatchReason, :idempotencyProcessed)
            ON CONFLICT (id) DO UPDATE SET
                event_id = EXCLUDED.event_id,
                version = EXCLUDED.version,
                status = EXCLUDED.status,
                source_reference = EXCLUDED.source_reference,
                source_system = EXCLUDED.source_system,
                amount = EXCLUDED.amount,
                currency = EXCLUDED.currency,
                transaction_date = EXCLUDED.transaction_date,
                updated_at = EXCLUDED.updated_at,
                matched_movements = EXCLUDED.matched_movements,
                mismatch_reason = EXCLUDED.mismatch_reason,
                idempotency_processed = EXCLUDED.idempotency_processed
            RETURNING *
            """)
            .bind("id", reconciliation.getId())
            .bind("eventId", reconciliation.getEventId())
            .bind("version", reconciliation.getVersion())
            .bind("status", reconciliation.getStatus().name())
            .bind("sourceReference", reconciliation.getSourceReference())
            .bind("sourceSystem", reconciliation.getSourceSystem())
            .bind("amount", reconciliation.getAmount())
            .bind("currency", reconciliation.getCurrency())
            .bind("transactionDate", reconciliation.getTransactionDate())
            .bind("createdAt", reconciliation.getCreatedAt())
            .bind("updatedAt", reconciliation.getUpdatedAt())
            .bind("matchedMovements", matchedMovementsJson)
            .bind("mismatchReason", reconciliation.getMismatchReason() != null ? reconciliation.getMismatchReason() : "")
            .bind("idempotencyProcessed", reconciliation.isIdempotencyProcessed())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .doOnSuccess(saved -> log.info("Conciliación persistida exitosamente: {}", saved.getId()))
            .doOnError(error -> log.error("Error al persistir conciliación: {}", error.getMessage()));
    }

    public Mono<Reconciliation> findById(String id) {
        log.debug("Buscando conciliación por ID: {}", id);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.defer(() -> {
                log.warn("No se encontró conciliación con ID: {}", id);
                return Mono.empty();
            }));
    }

    public Mono<Reconciliation> findBySourceReferenceAndSourceSystem(String sourceReference, String sourceSystem) {
        log.debug("Buscando conciliación por referencia: {} y sistema: {}", sourceReference, sourceSystem);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE source_reference = :sourceReference AND source_system = :sourceSystem
            ORDER BY version DESC
            LIMIT 1
            """)
            .bind("sourceReference", sourceReference)
            .bind("sourceSystem", sourceSystem)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.empty());
    }

    public Flux<Reconciliation> findByStatus(Status status) {
        log.debug("Buscando conciliaciones con estado: {}", status);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE status = :status ORDER BY created_at DESC")
            .bind("status", status.name())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all();
    }

    public Mono<List<Reconciliation>> findByStatusPaginated(Status status, int limit, int offset) {
        log.debug("Buscando conciliaciones con estado: {}, límite: {}, offset: {}", status, limit, offset);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE status = :status 
            ORDER BY created_at DESC
            LIMIT :limit OFFSET :offset
            """)
            .bind("status", status.name())
            .bind("limit", limit)
            .bind("offset", offset)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all()
            .collectList();
    }

    public Mono<Long> countByStatus(Status status) {
        return databaseClient.sql("SELECT COUNT(*) FROM reconciliations WHERE status = :status")
            .bind("status", status.name())
            .map((row, metadata) -> ((Number) row.get("count")).longValue())
            .first();
    }

    public Mono<Boolean> existsById(String id) {
        return databaseClient.sql("SELECT 1 FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .map(rows -> rows > 0);
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando conciliación con ID: {}", id);
        
        return databaseClient.sql("DELETE FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .then()
            .doOnSuccess(v -> log.info("Conciliación eliminada: {}", id))
            .doOnError(error -> log.error("Error al eliminar conciliación: {}", error.getMessage()));
    }

    private Reconciliation mapRowToReconciliation(java.util.Map<String, Object> row) {
        String id = (String) row.get("id");
        String eventId = (String) row.get("event_id");
        Integer version = ((Number) row.get("version")).intValue();
        Status status = Status.valueOf((String) row.get("status"));
        String sourceReference = (String) row.get("source_reference");
        String sourceSystem = (String) row.get("source_system");
        BigDecimal amount = (BigDecimal) row.get("amount");
        String currency = (String) row.get("currency");
        Instant transactionDate = (Instant) row.get("transaction_date");
        Instant createdAt = (Instant) row.get("created_at");
        Instant updatedAt = (Instant) row.get("updated_at");
        Boolean idempotencyProcessed = (Boolean) row.get("idempotency_processed");
        
        return new Reconciliation(
            id, eventId, version, status,
            sourceReference, sourceSystem, amount, currency,
            transactionDate, createdAt, updatedAt
        );
    }

    private String serializeMatchedMovements(List<MovementMatch> matchedMovements) {
        if (matchedMovements == null || matchedMovements.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < matchedMovements.size(); i++) {
            MovementMatch match = matchedMovements.get(i);
            json.append(String.format("{\"source\":\"%s\",\"reference\":\"%s\",\"amount\":%s}",
                match.source(), match.reference(), match.amount()));
            if (i < matchedMovements.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}