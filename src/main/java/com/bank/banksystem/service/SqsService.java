package com.bank.banksystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${aws.sqs.region}")
    private String region;

    public String sendMessage(String message) {

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .build();

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();

        sqsClient.sendMessage(request);

        return "Message sent successfully: " + message;
    }
}
