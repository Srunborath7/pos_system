package com.example.pos_system.controller;

import com.example.pos_system.dto.CustomerRequest;
import com.example.pos_system.model.Customer;
import com.example.pos_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request, Authentication authentication) {
        String username = authentication.getName(); // authenticated user
        Customer customer = customerService.createCustomer(request, username);
        return ResponseEntity.ok(customer);
    }
    // GET list all customers
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // GET customer by id (optional)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                            @RequestBody CustomerRequest request,
                                            Authentication authentication) {
        String username = authentication.getName();
        Customer updated = customerService.updateCustomer(id, request, username);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Add delete endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        boolean deleted = customerService.deleteCustomer(id, username);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
