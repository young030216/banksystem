package com.bank.banksystem.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.model.TranscationStatus;
import com.bank.banksystem.repository.TransactionRepository;
@RestController
@RequestMapping("/transactions")
public class TranscationController {
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get all transactions
     */
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transaction by transactionId
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionByTransactionId(@PathVariable String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    /**
     * Get transactions by fromUserName
     */
    @GetMapping("/from/{fromUserName}")
    public ResponseEntity<List<Transaction>> getTransactionsByFromUserName(@PathVariable String fromUserName) {
        List<Transaction> transactions = transactionRepository.findByFromUser_username(fromUserName);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Get transactions by toUserName
     */
    @GetMapping("/to/{toUserName}")
    public ResponseEntity<List<Transaction>> getTransactionsByToUserName(@PathVariable String toUserName) {
        List<Transaction> transactions = transactionRepository.findByToUser_username(toUserName);
        return ResponseEntity.ok(transactions); 
    }
    /**
     * Get transactions by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Transaction>> getTransactionsByStatus(@PathVariable TranscationStatus status) {
        List<Transaction> transactions = transactionRepository.findByStatus(status);
        return ResponseEntity.ok(transactions);
    }

}
