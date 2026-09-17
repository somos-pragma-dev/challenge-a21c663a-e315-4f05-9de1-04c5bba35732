package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMovementListenerTest {

    @Mock
    private ProcessMovementUseCase processMovementUseCase;

    private ObjectMapper objectMapper;
    private KafkaMovementListener listener;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        listener = new KafkaMovementListener(processMovementUseCase, objectMapper);
    }

    @Nested
    @DisplayName("Consumo de mensajes desde Kafka")
    class MessageConsumption {

        @Test
        @DisplayName("Debe procesar mensaje válido correctamente")
        void shouldProcessValidMessage() {
            String json = """
                {
                    "sourceReference": "TXN-KAFKA-001",
                    "sourceSystem": "CORE_BANK",
                    "amount": 2500.75,
                    "currency": "USD",
                    "transactionDate": "2024-01-15T10:30:00Z",
                    "eventId": "evt-001",
                    "version": 1
                }
                """;

            Reconciliation expectedResult = new Reconciliation(
                "TXN-KAFKA-001", "CORE_BANK", new BigDecimal("2500.75"), "USD", Instant.parse("2024-01-15T10:30:00Z")
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.just(expectedResult));

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for message processing");
            }

            ArgumentCaptor<ProcessMovementCommand> captor = ArgumentCaptor.forClass(ProcessMovementCommand.class);
            verify(processMovementUseCase, times(1)).execute(captor.capture());

            ProcessMovementCommand captured = captor.getValue();
            assertEquals("TXN-KAFKA-001", captured.sourceReference());
            assertEquals("CORE_BANK", captured.sourceSystem());
            assertEquals(new BigDecimal("2500.75"), captured.amount());
        }

        @Test
        @DisplayName("Debe manejar error de procesamiento gracefully")
        void shouldHandleProcessingError() {
            String json = """
                {
                    "sourceReference": "TXN-ERROR-001",
                    "sourceSystem": "INVALID_SYSTEM",
                    "amount": 100.00,
                    "currency": "USD",
                    "transactionDate": "2024-01-15T10:30:00Z",
                    "eventId": "evt-002",
                    "version": 1
                }
                """;

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.error(new RuntimeException("Invalid source system")));

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for error handling");
            }

            verify(processMovementUseCase, times(1)).execute(any(ProcessMovementCommand.class));
        }

        @Test
        @DisplayName("Debe procesar lote de mensajes correctamente")
        void shouldProcessBatchOfMessages() throws Exception {
            List<String> jsons = List.of(
                """{"sourceReference":"TXN-001","sourceSystem":"CORE_BANK","amount":100,"currency":"USD","transactionDate":"2024-01-15T10:30:00Z","eventId":"evt-001","version":1}""",
                """{"sourceReference":"TXN-002","sourceSystem":"PAYMENT_GW","amount":200,"currency":"USD","transactionDate":"2024-01-15T11:30:00Z","eventId":"evt-002","version":1}""",
                """{"sourceReference":"TXN-003","sourceSystem":"LIQUIDATION","amount":300,"currency":"USD","transactionDate":"2024-01-15T12:30:00Z","eventId":"evt-003","version":1}"""
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenAnswer(inv -> {
                    ProcessMovementCommand cmd = inv.getArgument(0);
                    Reconciliation r = new Reconciliation(
                        cmd.sourceReference(), cmd.sourceSystem(), cmd.amount(), cmd.currency(), cmd.transactionDate()
                    );
                    return Mono.just(r);
                });

            CountDownLatch latch = new CountDownLatch(3);

            for (String json : jsons) {
                listener.listenMovement(json, null)
                    .doOnTerminate(latch::countDown)
                    .subscribe();
            }

            try {
                assertTrue(latch.await(10, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for batch processing");
            }

            verify(processMovementUseCase, times(3)).execute(any(ProcessMovementCommand.class));
        }
    }

    @Nested
    @DisplayName("Integración con validación")
    class ValidationIntegration {

        @Test
        @DisplayName("Debe rechazar mensaje con JSON inválido")
        void shouldRejectInvalidJson() {
            String invalidJson = "{ invalid json structure ";

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(invalidJson, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for invalid JSON handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Debe manejar mensaje con campos faltantes")
        void shouldHandleMissingFields() {
            String json = """
                {
                    "sourceReference": "TXN-MISSING-001"
                }
                """;

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for missing fields handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("Control de flujo y backpressure")
    class FlowControl {

        @Test
        @DisplayName("Debe completar incluso con mensaje null")
        void shouldCompleteWithNullMessage() {
            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(null, null)
                .doOnTerminate(latch::countDown)
                .subscribe();

            try {
                assertTrue(latch.await(2, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for null message");
            }

            verifyNoInteractions(processMovementUseCase);
        }
    }
}