package com.example.pos_system.service;

import com.example.pos_system.dto.CustomerRequest;
import com.example.pos_system.model.Customer;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.CustomerRepository;
import com.example.pos_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Customer createCustomer(CustomerRequest request, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Creator user not found"));

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCreatedBy(creator);

        return customerRepository.save(customer);
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
    @Transactional
    public Customer updateCustomer(Long id, CustomerRequest request, String updaterUsername) {
        return customerRepository.findById(id).map(existingCustomer -> {
            // Optionally verify updater user here if needed
            existingCustomer.setName(request.getName());
            existingCustomer.setEmail(request.getEmail());
            existingCustomer.setPhone(request.getPhone());
            existingCustomer.setAddress(request.getAddress());
            return customerRepository.save(existingCustomer);
        }).orElse(null);
    }

    @Transactional
    public boolean deleteCustomer(Long id, String deleterUsername) {
        return customerRepository.findById(id).map(customer -> {
            // Optionally verify deleter user here if needed
            customerRepository.delete(customer);
            return true;
        }).orElse(false);
    }

}
