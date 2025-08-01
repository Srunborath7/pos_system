package com.example.pos_system.controller;

import com.example.pos_system.dto.OrderRequest;
import com.example.pos_system.model.Order;
import com.example.pos_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        try {
            Order order = orderService.createOrder(req);
            return ResponseEntity.ok(Map.of("message", "Order saved", "orderId", order.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
