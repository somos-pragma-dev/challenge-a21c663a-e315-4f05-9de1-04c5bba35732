package com.pragma.reconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada principal del Motor de Conciliación Bancaria en Tiempo Real.
 * 
 * Esta aplicación consume streams de movimientos desde tres fuentes:
 * - Core bancario
 * - Gateway de pagos  
 * - Sistema de liquidación
 * 
 * Detecta discrepancias en ventanas móviles y gestiona el ciclo de vida
 * de cada conciliación (Pending → Matched/Mismatched/Manual).
 * 
 * Arquitectura: Hexagonal/Clean con CQRS y Event Sourcing
 * Stack: Spring Boot 3.4 + WebFlux + Kafka + PostgreSQL (R2DBC)
 */
@SpringBootApplication
@EnableKafka
@EnableR2dbcRepositories(basePackages = "com.pragma.reconciliation.infrastructure.adapters.outbound")
@EntityScan(basePackages = "com.pragma.reconciliation.domain.model")
public class ReconciliationApplication {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationApplication.class);

    public static void main(String[] args) {
        log.info("========================================================");
        log.info("Iniciando Motor de Conciliación Bancaria en Tiempo Real");
        log.info("Versión: 1.0.0-SNAPSHOT");
        log.info("Java Version: {}", System.getProperty("java.version"));
        log.info("========================================================");
        
        SpringApplication.run(ReconciliationApplication.class, args);
        
        log.info("========================================================");
        log.info("Aplicación iniciada correctamente");
        log.info("Endpoints de actuator disponibles en: /actuator/*");
        log.info("========================================================");
    }

    /**
     * Configuración del factory de listeners de Kafka para procesamiento reactivo.
     * Configura manejo de errores con reintentos y dead-letter topic.
     */
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            ProducerFactory<String, String> producerFactory) {
        
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // Configuración para procesamiento concurrente
        factory.setConcurrency(3);
        
        // Configuración de acknowledgement
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        
        // Manejo de errores con reintentos
        FixedBackOff backOff = new FixedBackOff(1000L, 3L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);
        factory.setCommonErrorHandler(errorHandler);
        
        log.info("Kafka listener container factory configurado con concurrencia=3 y reintentos=3");
        
        return factory;
    }
}