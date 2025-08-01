package com.example.pos_system.controller;

import com.example.pos_system.dto.OrderRequest;
import com.example.pos_system.model.Order;
import com.example.pos_system.repository.OrderRepository;
import com.example.pos_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        try {
            Order order = orderService.createOrder(req);
            return ResponseEntity.ok(Map.of("message", "Order saved", "orderId", order.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getOrderCount() {
        long count = orderRepository.count();
        return ResponseEntity.ok(count);
    }
}
