package com.bank.banksystem.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.model.TranscationStatus;
import com.bank.banksystem.repository.TransactionRepository;
import com.bank.banksystem.service.PdfLambdaService;
@RestController
@RequestMapping("/transactions")
public class TranscationController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PdfLambdaService pdfLambdaService;

    /**
     * Generate PDF by database ID
     */
    @PostMapping("/{id}/generate-pdf")
    public ResponseEntity<?> generatePdf(@PathVariable Long id) throws Exception {

        // 1. find transaction
        Transaction tx = transactionRepository.findById(id)
                .orElse(null);

        if (tx == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. call Lambda
        String lambdaResponse = pdfLambdaService.generatePdf(tx);

        // 3. return Lambda JSON response
        return ResponseEntity.ok(lambdaResponse);
    }

    /**
     * Other endpoints unchanged
     */
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionByTransactionId(@PathVariable String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/from/{fromUserName}")
    public ResponseEntity<List<Transaction>> getTransactionsByFromUserName(@PathVariable String fromUserName) {
        List<Transaction> transactions = transactionRepository.findByFromUser_username(fromUserName);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/to/{toUserName}")
    public ResponseEntity<List<Transaction>> getTransactionsByToUserName(@PathVariable String toUserName) {
        List<Transaction> transactions = transactionRepository.findByToUser_username(toUserName);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Transaction>> getTransactionsByStatus(@PathVariable TranscationStatus status) {
        List<Transaction> transactions = transactionRepository.findByStatus(status);
        return ResponseEntity.ok(transactions);
    }
}