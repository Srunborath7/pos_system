package com.example.pos_system.controller;

import com.example.pos_system.model.Invoice;
import com.example.pos_system.model.Order;
import com.example.pos_system.repository.OrderRepository;
import com.example.pos_system.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final OrderRepository orderRepository;

    @PostMapping("/{orderId}/generate")  // only this path
    public ResponseEntity<?> generateInvoicePdf(@PathVariable Long orderId) {
        invoiceService.generateInvoicePdf(orderId);
        return ResponseEntity.ok(Map.of("message", "Invoice generated"));
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<?> getInvoice(@PathVariable Long id) {
//        return ResponseEntity.of(invoiceService.getById(id));
//    }
}
