package com.example.pos_system.service;

import com.example.pos_system.model.Inventory;
import com.example.pos_system.model.Product;
import com.example.pos_system.repository.InventoryRepository;
import com.example.pos_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with ID: " + id));
    }

    @Transactional
    public Inventory addInventory(Long productId, int quantity, Inventory.Action action, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        int currentStock = product.getStock();
        int newStock = action == Inventory.Action.IN
                ? currentStock + quantity
                : currentStock - quantity;

        if (action == Inventory.Action.OUT && currentStock < quantity) {
            throw new IllegalArgumentException("Insufficient stock to remove");
        }

        product.setStock(newStock);
        productRepository.save(product);

        Inventory inventory = Inventory.builder()
                .product(product)
                .quantity(quantity)
                .action(action)
                .description(description)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory updateInventory(Long id, Long productId, int quantity, Inventory.Action action, String description) {
        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with ID: " + id));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // No stock re-calculation here for safety; handle with caution if enabling
        existing.setProduct(product);
        existing.setQuantity(quantity);
        existing.setAction(action);
        existing.setDescription(description);

        return inventoryRepository.save(existing);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
