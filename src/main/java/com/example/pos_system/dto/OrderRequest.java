package com.example.pos_system.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private double totalPrice;
    private String customerName;
    private String status;
    private List<OrderItemRequest> items;
    private Long createdBy; // user id
}
