package com.bank.banksystem.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.bank.banksystem.dto.LoginRequest;

import com.bank.banksystem.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if ("young_test".equals(request.getUsername()) && "young_test".equals(request.getPassword())) {
            Map<String, Object> claims = Map.of("role", "admin");
            String token = JwtUtil.generateToken(request.getUsername(), claims);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username or password is incorrect");
        }
    }
}
