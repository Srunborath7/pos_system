package com.example.pos_system.config;

import com.example.pos_system.model.Role;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.RoleRepository;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Step 1: Create default roles
            List<String> roles = List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
            for (String roleName : roles) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
            }

            // Step 2: Create default admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("Admin role not found"));

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("12345"));
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                System.out.println("Default admin user created.");
            }
        };
    }
}
