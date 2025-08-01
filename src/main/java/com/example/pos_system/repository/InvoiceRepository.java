package com.example.pos_system.repository;

import com.example.pos_system.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrderId(Long orderId);
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i")
    Double getTotalRevenue();
}
