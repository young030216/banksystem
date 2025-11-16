package com.bank.banksystem.controller;

import com.bank.banksystem.service.SqsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private final SqsService sqsService;

    public SqsController(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping("/send")
    public String send(@RequestParam String message) {
        return sqsService.sendMessage(message);
    }

    @GetMapping("/receive")
    public ResponseEntity<String> receiveMessage() {
        return ResponseEntity.ok(sqsService.receiveMessage());
    }
}
