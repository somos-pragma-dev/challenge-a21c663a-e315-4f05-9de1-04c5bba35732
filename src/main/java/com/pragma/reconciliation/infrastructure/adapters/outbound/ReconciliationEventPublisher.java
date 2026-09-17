package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Created;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewResolved;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.IdempotentProcessed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ReconciliationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String eventsTopic;

    public ReconciliationEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${reconciliation.kafka.topics.events:reconciliation.events}") String eventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.eventsTopic = eventsTopic;
    }

    public reactor.core.publisher.Mono<Void> publish(ReconciliationEvent event) {
        return reactor.core.publisher.Mono.fromCallable(() -> serializeEvent(event))
                .flatMap(this::sendToKafka)
                .doOnSuccess(result -> log.debug("Event published successfully: type={}, reconciliationId={}",
                        event.getClass().getSimpleName(), event.reconciliationId()))
                .doOnError(error -> log.error("Failed to publish event: type={}, reconciliationId={}, error={}",
                        event.getClass().getSimpleName(), event.reconciliationId(), error.getMessage()));
    }

    private String serializeEvent(ReconciliationEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event: " + event.reconciliationId(), e);
        }
    }

    private reactor.core.publisher.Mono<Void> sendToKafka(String serializedEvent) {
        return reactor.core.publisher.Mono.create(sink -> {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(eventsTopic,
                    extractReconciliationId(serializedEvent), serializedEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Kafka send failed: {}", ex.getMessage(), ex);
                    sink.error(ex);
                } else {
                    log.trace("Kafka send success: partition={}, offset={}",
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    sink.success();
                }
            });
        });
    }

    private String extractReconciliationId(String serializedEvent) {
        try {
            return objectMapper.readTree(serializedEvent).get("reconciliationId").asText();
        } catch (Exception e) {
            log.warn("Could not extract reconciliationId from event, using default key");
            return "unknown";
        }
    }

    public reactor.core.publisher.Mono<Void> publishCreated(Created event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMatched(Matched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMismatched(Mismatched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewEscalation(ManualReviewEscalation event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewResolved(ManualReviewResolved event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishIdempotentProcessed(IdempotentProcessed event) {
        return publish(event);
    }
}