package com.example.pos_system.controller;

import com.example.pos_system.dto.PaymentRequest;
import com.example.pos_system.model.Payment;
import com.example.pos_system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequest request) {
        if (request.getOrderId() == null || request.getAmountReceived() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        try {
            Payment payment = paymentService.makePayment(request.getOrderId(), request.getAmountReceived());

            return ResponseEntity.ok(Map.of(
                    "message", "Payment successful",
                    "paymentId", payment.getId(),
                    "change", payment.getChangeGiven()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
