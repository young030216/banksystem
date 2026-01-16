package com.bank.banksystem.service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class LatencyService {

    private final Timer latencyTimer;

    public LatencyService(MeterRegistry meterRegistry) {
        this.latencyTimer = Timer.builder("custom_latency")
                .description("Custom latency metric")
                .register(meterRegistry);
    }

    public void simulateWork() throws InterruptedException {
        long start = System.nanoTime();
        
        Thread.sleep((long) (Math.random() * 500));

        long end = System.nanoTime();
        latencyTimer.record(end - start, TimeUnit.NANOSECONDS);
    }
}