package com.example.pos_system.service;

import com.example.pos_system.dto.OrderItemRequest;
import com.example.pos_system.dto.OrderRequest;
import com.example.pos_system.model.Order;
import com.example.pos_system.model.OrderItem;
import com.example.pos_system.model.Product;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.OrderRepository;
import com.example.pos_system.repository.ProductRepository;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Order createOrder(OrderRequest req) {
        User creator = userRepository.findById(req.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .customerName(req.getCustomerName())
                .status(req.getStatus())
                .totalPrice(req.getTotalPrice())
                .createdBy(creator)
                .build();

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemReq : req.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            product.setStock(product.getStock() - itemReq.getQuantity());

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .price(itemReq.getPrice())
                    .order(order)
                    .build();

            items.add(item);
        }

        order.setOrderItems(items);
        return orderRepository.save(order);
    }
}
