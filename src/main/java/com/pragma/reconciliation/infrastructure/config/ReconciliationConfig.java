package com.pragma.reconciliation.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Configuración del módulo de conciliación.
 * Define parámetros para matching, SLA, procesamiento y alerting.
 */
@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationConfig {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationConfig.class);
    
    private Matching matching;
    private Sla sla;
    private Processing processing;
    private Alerting alerting;
    private Idempotency idempotency;
    
    public ReconciliationConfig() {
        this.matching = new Matching();
        this.sla = new Sla();
        this.processing = new Processing();
        this.alerting = new Alerting();
        this.idempotency = new Idempotency();
    }
    
    public Matching getMatching() {
        return matching;
    }
    
    public void setMatching(Matching matching) {
        this.matching = matching;
    }
    
    public Sla getSla() {
        return sla;
    }
    
    public void setSla(Sla sla) {
        this.sla = sla;
    }
    
    public Processing getProcessing() {
        return processing;
    }
    
    public void setProcessing(Processing processing) {
        this.processing = processing;
    }
    
    public Alerting getAlerting() {
        return alerting;
    }
    
    public void setAlerting(Alerting alerting) {
        this.alerting = alerting;
    }
    
    public Idempotency getIdempotency() {
        return idempotency;
    }
    
    public void setIdempotency(Idempotency idempotency) {
        this.idempotency = idempotency;
    }
    
    public long getMatchingWindowMinutes() {
        return matching != null ? matching.windowMinutes : 60;
    }
    
    public BigDecimal getAmountTolerance() {
        return matching != null ? matching.tolerance : BigDecimal.ZERO;
    }
    
    public long getSlaMinutes() {
        return sla != null ? sla.minutes : 1440;
    }
    
    public void logConfiguration() {
        log.info("=== Configuración de Conciliación ===");
        log.info("Matching: ventana={} minutos, tolerancia={}", 
            getMatchingWindowMinutes(), getAmountTolerance());
        log.info("SLA: {} minutos", getSlaMinutes());
        log.info("Procesamiento: threads={}, batchSize={}", 
            processing != null ? processing.threads : 4,
            processing != null ? processing.batchSize : 100);
        log.info("Idempotencia: enabled={}, ttl={} horas", 
            idempotency != null ? idempotency.enabled : true,
            idempotency != null ? idempotency.ttlHours : 24);
    }
    
    public static class Matching {
        private long windowMinutes = 60;
        private BigDecimal tolerance = BigDecimal.valueOf(0.01);
        private boolean enablePartialMatch = true;
        
        public long getWindowMinutes() {
            return windowMinutes;
        }
        
        public void setWindowMinutes(long windowMinutes) {
            this.windowMinutes = windowMinutes;
        }
        
        public BigDecimal getTolerance() {
            return tolerance;
        }
        
        public void setTolerance(BigDecimal tolerance) {
            this.tolerance = tolerance;
        }
        
        public boolean isEnablePartialMatch() {
            return enablePartialMatch;
        }
        
        public void setEnablePartialMatch(boolean enablePartialMatch) {
            this.enablePartialMatch = enablePartialMatch;
        }
    }
    
    public static class Sla {
        private long minutes = 1440;
        private boolean enabled = true;
        private boolean criticalEscalation = true;
        
        public long getMinutes() {
            return minutes;
        }
        
        public void setMinutes(long minutes) {
            this.minutes = minutes;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isCriticalEscalation() {
            return criticalEscalation;
        }
        
        public void setCriticalEscalation(boolean criticalEscalation) {
            this.criticalEscalation = criticalEscalation;
        }
    }
    
    public static class Processing {
        private int threads = 4;
        private int batchSize = 100;
        private boolean asyncEnabled = true;
        
        public int getThreads() {
            return threads;
        }
        
        public void setThreads(int threads) {
            this.threads = threads;
        }
        
        public int getBatchSize() {
            return batchSize;
        }
        
        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
        
        public boolean isAsyncEnabled() {
            return asyncEnabled;
        }
        
        public void setAsyncEnabled(boolean asyncEnabled) {
            this.asyncEnabled = asyncEnabled;
        }
    }
    
    public static class Alerting {
        private boolean enabled = true;
        private String webhookUrl;
        private int warningThresholdPercent = 80;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getWebhookUrl() {
            return webhookUrl;
        }
        
        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }
        
        public int getWarningThresholdPercent() {
            return warningThresholdPercent;
        }
        
        public void setWarningThresholdPercent(int warningThresholdPercent) {
            this.warningThresholdPercent = warningThresholdPercent;
        }
    }
    
    public static class Idempotency {
        private boolean enabled = true;
        private int ttlHours = 24;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public int getTtlHours() {
            return ttlHours;
        }
        
        public void setTtlHours(int ttlHours) {
            this.ttlHours = ttlHours;
        }
    }
}