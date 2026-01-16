package com.bank.banksystem;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
public class CloudWatchConfigClass {

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry() {
        CloudWatchConfig cloudWatchConfig = new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String namespace() {
                return "MyAppMetrics";
            }
        };

        CloudWatchAsyncClient asyncClient = CloudWatchAsyncClient.create();

        return new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM, asyncClient);
    }
}
