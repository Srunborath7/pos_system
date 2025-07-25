package com.example.pos_system.service;

import com.example.pos_system.model.Inventory;
import com.example.pos_system.model.Product;
import com.example.pos_system.repository.InventoryRepository;
import com.example.pos_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Transactional
    public Inventory addInventory(Long productId, int quantity, Inventory.Action action, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // Update stock according to action
        int currentStock = product.getStock();
        int newStock = currentStock;
        if (action == Inventory.Action.IN) {
            newStock = currentStock + quantity;
        } else if (action == Inventory.Action.OUT) {
            if (currentStock < quantity) {
                throw new IllegalArgumentException("Insufficient stock to remove");
            }
            newStock = currentStock - quantity;
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

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
