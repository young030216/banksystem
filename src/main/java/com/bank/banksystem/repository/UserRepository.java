package com.bank.banksystem.repository;

import com.bank.banksystem.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


@Repository
public interface  UserRepository extends JpaRepository<User, Long> {
    /**
     * Get user by username
     */
    Optional<User> findByUsername(String username);
    /**
     * Get user by phone
     */
    Optional<User> findByPhone(Long phone);
}
