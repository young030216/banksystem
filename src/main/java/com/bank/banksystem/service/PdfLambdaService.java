package com.bank.banksystem.service;

import com.bank.banksystem.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Service
public class PdfLambdaService {

    private final LambdaClient lambdaClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public PdfLambdaService() {
        this.lambdaClient = LambdaClient.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();
    }

    public String generatePdf(Transaction tx) throws Exception {

        String payloadJson = mapper.writeValueAsString(tx);

        InvokeRequest req = InvokeRequest.builder()
                .functionName("transaction_pdf_lambda")
                .payload(SdkBytes.fromUtf8String(payloadJson))
                .build();

        InvokeResponse resp = lambdaClient.invoke(req);
        return resp.payload().asUtf8String();
    }
}
