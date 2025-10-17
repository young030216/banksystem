package com.bank.banksystem.repository;

import org.springframework.stereotype.Repository;
import com.bank.banksystem.model.TranscationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.bank.banksystem.model.Transaction;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Get transaction by transactionId
     */


    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByFromUser_username(String fromUserName);
    List<Transaction> findByToUser_username(String toUserName);
    List<Transaction> findByStatus(TranscationStatus status);
    

    
    
}
