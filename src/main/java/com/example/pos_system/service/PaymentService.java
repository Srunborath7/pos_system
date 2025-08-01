package com.example.pos_system.service;

import com.example.pos_system.model.Order;
import com.example.pos_system.model.Payment;
import com.example.pos_system.repository.OrderRepository;
import com.example.pos_system.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Payment makePayment(Long orderId, Double amountReceived) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID must not be null");
        }
        if (amountReceived == null) {
            throw new IllegalArgumentException("Amount received must not be null");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));

        double total = order.getTotalPrice();
        double change = amountReceived - total;

        if (change < 0) {
            throw new RuntimeException("Insufficient cash provided");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountReceived(amountReceived);
        payment.setChangeGiven(change);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        order.setStatus("PAID");
        orderRepository.save(order);

        return payment;
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }
}
