package com.example.pos_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_received", nullable = false)
    private double amountReceived;

    @Column(name = "change_given", nullable = false)
    private double changeGiven;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @PrePersist
    protected void onPay() {
        this.paidAt = LocalDateTime.now();
    }
}
