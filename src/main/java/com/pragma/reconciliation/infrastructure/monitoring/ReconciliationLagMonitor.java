package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReconciliationLagMonitor {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationLagMonitor.class);

    private final ReconciliationConfig config;
    private final LagAlertPublisher alertPublisher;
    private final Map<String, LagMetrics> metricsBySource = new ConcurrentHashMap<>();
    private final AtomicInteger totalProcessed = new AtomicInteger(0);
    private final AtomicInteger totalPending = new AtomicInteger(0);
    private final AtomicInteger totalMatched = new AtomicInteger(0);
    private final AtomicInteger totalMismatched = new AtomicInteger(0);
    private final AtomicInteger totalManualReview = new AtomicInteger(0);
    private final AtomicInteger slaWarnings = new AtomicInteger(0);
    private final AtomicInteger slaCritical = new AtomicInteger(0);
    private final AtomicLong lastAlertTime = new AtomicLong(0);

    public ReconciliationLagMonitor(ReconciliationConfig config, LagAlertPublisher alertPublisher) {
        this.config = config;
        this.alertPublisher = alertPublisher;
        initializeSourceMetrics();
    }

    private void initializeSourceMetrics() {
        metricsBySource.put("CORE_BANKING", new LagMetrics("CORE_BANKING"));
        metricsBySource.put("PAYMENT_GATEWAY", new LagMetrics("PAYMENT_GATEWAY"));
        metricsBySource.put("LIQUIDATION_SYSTEM", new LagMetrics("LIQUIDATION_SYSTEM"));
    }

    public void recordProcessing(String sourceSystem, int processedCount, int pendingCount) {
        LagMetrics metrics = metricsBySource.get(sourceSystem);
        if (metrics != null) {
            metrics.recordProcessing(processedCount, pendingCount);
        }
        totalProcessed.addAndGet(processedCount);
        totalPending.set(pendingCount);
    }

    public void recordReconciliationStatus(Reconciliation.Status status) {
        switch (status) {
            case PENDING -> totalPending.incrementAndGet();
            case MATCHED -> totalMatched.incrementAndGet();
            case MISMATCHED -> totalMismatched.incrementAndGet();
            case MANUAL_REVIEW -> totalManualReview.incrementAndGet();
            default -> log.warn("Unknown reconciliation status: {}", status);
        }
    }

    public void recordSlaBreach(String reconciliationId, Duration age, boolean isCritical) {
        if (isCritical) {
            slaCritical.incrementAndGet();
            log.warn("CRITICAL SLA breach for reconciliation: {}, age: {} minutes",
                    reconciliationId, age.toMinutes());
        } else {
            slaWarnings.incrementAndGet();
            log.warn("SLA warning for reconciliation: {}, age: {} minutes",
                    reconciliationId, age.toMinutes());
        }
        checkAndPublishAlert(reconciliationId, age, isCritical);
    }

    @Scheduled(fixedRateString = "${reconciliation.sla.alert-check-interval-seconds:60}000")
    public void performScheduledLagCheck() {
        if (!config.getAlerting().isEnabled() || !config.getSla().isEnableAutoEscalation()) {
            return;
        }
        log.debug("Performing scheduled lag check");
        checkLagThresholds();
    }

    private void checkLagThresholds() {
        Duration warningThreshold = config.getSla().getWarningDuration();
        Duration criticalThreshold = config.getSla().getCriticalDuration();

        metricsBySource.values().forEach(metrics -> {
            Duration currentLag = metrics.getCurrentLag();
            if (currentLag.compareTo(criticalThreshold) >= 0) {
                publishSystemAlert("CRITICAL", metrics.getSource(), currentLag, metrics.getPendingCount());
            } else if (currentLag.compareTo(warningThreshold) >= 0) {
                publishSystemAlert("WARNING", metrics.getSource(), currentLag, metrics.getPendingCount());
            }
        });

        logLagMetrics();
    }

    private void checkAndPublishAlert(String reconciliationId, Duration age, boolean isCritical) {
        long currentTime = System.currentTimeMillis();
        long cooldownMs = config.getAlerting().getAlertCooldownMinutes() * 60 * 1000L;

        if (currentTime - lastAlertTime.get() < cooldownMs) {
            log.debug("Alert cooldown active, skipping SLA alert for: {}", reconciliationId);
            return;
        }

        lastAlertTime.set(currentTime);

        if (alertPublisher != null) {
            alertPublisher.publishSlaAlert(reconciliationId, age, isCritical);
        }
    }

    private void publishSystemAlert(String severity, String source, Duration lag, int pendingCount) {
        log.warn("System alert: {} - Source: {}, Lag: {} minutes, Pending: {}",
                severity, source, lag.toMinutes(), pendingCount);

        if (alertPublisher != null) {
            alertPublisher.publishSystemLagAlert(severity, source, lag, pendingCount);
        }
    }

    public LagSnapshot getCurrentSnapshot() {
        return new LagSnapshot(
                Instant.now(),
                totalProcessed.get(),
                totalPending.get(),
                totalMatched.get(),
                totalMismatched.get(),
                totalManualReview.get(),
                slaWarnings.get(),
                slaCritical.get(),
                Map.copyOf(metricsBySource)
        );
    }

    public Map<String, LagMetrics> getMetricsBySource() {
        return Map.copyOf(metricsBySource);
    }

    public int getTotalProcessed() {
        return totalProcessed.get();
    }

    public int getTotalPending() {
        return totalPending.get();
    }

    public int getTotalMatched() {
        return totalMatched.get();
    }

    public int getTotalMismatched() {
        return totalMismatched.get();
    }

    public int getTotalManualReview() {
        return totalManualReview.get();
    }

    public int getSlaWarnings() {
        return slaWarnings.get();
    }

    public int getSlaCritical() {
        return slaCritical.get();
    }

    private void logLagMetrics() {
        log.info("=== Reconciliation Lag Metrics ===");
        log.info("Total Processed: {}, Pending: {}, Matched: {}, Mismatched: {}, Manual: {}",
                totalProcessed.get(), totalPending.get(), totalMatched.get(),
                totalMismatched.get(), totalManualReview.get());
        log.info("SLA Warnings: {}, Critical: {}", slaWarnings.get(), slaCritical.get());
        metricsBySource.forEach((source, metrics) ->
                log.info("Source {}: Lag={}min, Processed={}, Pending={}",
                        source, metrics.getCurrentLag().toMinutes(),
                        metrics.getProcessedCount(), metrics.getPendingCount()));
    }

    public static class LagMetrics {
        private final String source;
        private final AtomicInteger processedCount = new AtomicInteger(0);
        private final AtomicInteger pendingCount = new AtomicInteger(0);
        private volatile Instant lastProcessingTime;
        private volatile Duration currentLag = Duration.ZERO;

        public LagMetrics(String source) {
            this.source = source;
        }

        public void recordProcessing(int processed, int pending) {
            processedCount.addAndGet(processed);
            pendingCount.set(pending);
            lastProcessingTime = Instant.now();
            calculateLag();
        }

        private void calculateLag() {
            if (lastProcessingTime != null) {
                currentLag = Duration.between(lastProcessingTime, Instant.now());
            }
        }

        public String getSource() {
            return source;
        }

        public int getProcessedCount() {
            return processedCount.get();
        }

        public int getPendingCount() {
            return pendingCount.get();
        }

        public Instant getLastProcessingTime() {
            return lastProcessingTime;
        }

        public Duration getCurrentLag() {
            calculateLag();
            return currentLag;
        }
    }

    public record LagSnapshot(
            Instant timestamp,
            int totalProcessed,
            int totalPending,
            int totalMatched,
            int totalMismatched,
            int totalManualReview,
            int slaWarnings,
            int slaCritical,
            Map<String, LagMetrics> metricsBySource
    ) {}

    public interface LagAlertPublisher {
        void publishSlaAlert(String reconciliationId, Duration age, boolean isCritical);
        void publishSystemLagAlert(String severity, String source, Duration lag, int pendingCount);
    }
}