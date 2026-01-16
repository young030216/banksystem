package com.bank.banksystem.service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private final Counter eventCounter;

    public CounterService(MeterRegistry meterRegistry) {
        this.eventCounter = Counter.builder("custom_counter")
                .description("Counts events")
                .register(meterRegistry);
    }

    public void incrementEvent() {
        eventCounter.increment();
    }
}