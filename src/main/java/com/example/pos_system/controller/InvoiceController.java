package com.example.pos_system.controller;

import com.example.pos_system.model.Invoice;
import com.example.pos_system.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/{orderId}/generate")
    public ResponseEntity<?> generateInvoicePdf(
            @PathVariable Long orderId,
            @RequestBody Map<String, Double> paymentInfo
    ) {
        double cashReceived = paymentInfo.getOrDefault("cashReceived", 0.0);
        double changeReturned = paymentInfo.getOrDefault("changeReturned", 0.0);

        invoiceService.generateInvoicePdf(orderId, cashReceived, changeReturned);

        return ResponseEntity.ok(Map.of("message", "Invoice generated"));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<?> getInvoiceByOrderId(@PathVariable Long orderId) {
        Invoice invoice = invoiceService.getInvoiceByOrderId(orderId);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }
    @GetMapping("/total")
    public double getTotalRevenue() {
        return invoiceService.getTotalAmount();
    }
}
