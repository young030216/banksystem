package com.bank.banksystem.service;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GaugeService {

    private final AtomicInteger gaugeValue = new AtomicInteger(0);

    public GaugeService(MeterRegistry meterRegistry) {
        Gauge.builder("custom_gauge", gaugeValue, AtomicInteger::get)
                .description("Shows real-time value")
                .register(meterRegistry);
    }

    public void setValue(int value) {
        gaugeValue.set(value);
    }
}
