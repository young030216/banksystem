package com.bank.banksystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Service
public class SqsService {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${aws.sqs.region}")
    private String region;

    // Send message
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

    // Receive and delete message
    public String receiveMessage() {

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .build();

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(5) 
                .maxNumberOfMessages(1)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

        if (messages.isEmpty()) {
            return "No messages available";
        }

        Message msg = messages.get(0);

        // Auto delete after receiving
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(msg.receiptHandle())
                .build();

        sqsClient.deleteMessage(deleteRequest);

        return "Received message: " + msg.body();
    }
}
