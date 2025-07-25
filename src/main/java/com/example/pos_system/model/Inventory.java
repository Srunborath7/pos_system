package com.example.pos_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product reference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private Action action; // IN or OUT

    private String description;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Action {
        IN,
        OUT
    }
}
