package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand.MovementSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class KafkaMovementListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMovementListener.class);

    private final ProcessMovementUseCase processMovementUseCase;
    private final ObjectMapper objectMapper;

    public KafkaMovementListener(ProcessMovementUseCase processMovementUseCase, ObjectMapper objectMapper) {
        this.processMovementUseCase = processMovementUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovement(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement message: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                log.warn("Invalid message format, acknowledging to avoid redelivery: {}", message);
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.execute(command)
                    .doOnSuccess(reconciliation -> {
                        log.info("Movement processed successfully: reconciliationId={}, status={}",
                                reconciliation.getId(), reconciliation.getStatus());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error processing movement: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Fatal error parsing movement message: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor-sla}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovementWithSla(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement for SLA processing: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.processWithSlaCheck(command)
                    .doOnSuccess(reconciliation -> {
                        log.debug("Movement processed with SLA check: reconciliationId={}",
                                reconciliation.getId());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error in SLA processing: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Error in SLA listener: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private ProcessMovementCommand parseAndValidateMessage(String message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(message, Map.class);

            String sourceReference = (String) payload.get("sourceReference");
            String sourceSystem = (String) payload.get("sourceSystem");
            String eventId = (String) payload.get("eventId");

            if (sourceReference == null || sourceSystem == null || eventId == null) {
                log.warn("Missing required fields in message: {}", message);
                return null;
            }

            BigDecimal amount = new BigDecimal(payload.get("amount").toString());
            String currency = (String) payload.get("currency");
            String transactionDateStr = (String) payload.get("transactionDate");
            int version = payload.containsKey("version")
                    ? ((Number) payload.get("version")).intValue()
                    : 1;

            Instant transactionDate = transactionDateStr != null
                    ? Instant.parse(transactionDateStr)
                    : Instant.now();

            MovementSource source = MovementSource.valueOf(sourceSystem.toUpperCase());

            return ProcessMovementCommand.create(sourceReference, sourceSystem, eventId,
                    amount, currency, transactionDate, version, source);

        } catch (Exception e) {
            log.error("Failed to parse movement message: {}", e.getMessage(), e);
            return null;
        }
    }
}