package com.example.pos_system.controller;

import com.example.pos_system.dto.InventoryRequest;
import com.example.pos_system.model.Inventory;
import com.example.pos_system.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inventoryService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addInventory(@RequestBody InventoryRequest request) {
        try {
            Inventory inventory = inventoryService.addInventory(
                    request.getProductId(),
                    request.getQuantity(),
                    request.getAction(),
                    request.getDescription()
            );
            return ResponseEntity.ok(inventory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody InventoryRequest request) {
        try {
            Inventory inventory = inventoryService.updateInventory(
                    id,
                    request.getProductId(),
                    request.getQuantity(),
                    request.getAction(),
                    request.getDescription()
            );
            return ResponseEntity.ok(inventory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok().build();
    }
}
