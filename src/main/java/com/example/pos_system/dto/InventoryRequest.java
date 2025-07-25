package com.example.pos_system.dto;

import com.example.pos_system.model.Inventory;

public class InventoryRequest {
    private Long productId;
    private int quantity;
    private Inventory.Action action;
    private String description;

    // getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Inventory.Action getAction() { return action; }
    public void setAction(Inventory.Action action) { this.action = action; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}