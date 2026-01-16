package com.bank.banksystem.controller;

import com.bank.banksystem.service.CounterService;
import com.bank.banksystem.service.GaugeService;
import com.bank.banksystem.service.LatencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {
    private final LatencyService latencyService;
    private final CounterService counterService;
    private final GaugeService gaugeService;

    public MetricsController(LatencyService latencyService, CounterService counterService, GaugeService gaugeService) {
        this.latencyService = latencyService;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @GetMapping("/simulate")
    public String simulate() throws InterruptedException {
        latencyService.simulateWork();
        counterService.incrementEvent();
        gaugeService.setValue((int)(Math.random() * 100));
        return "Simulation done!";
    }
}
