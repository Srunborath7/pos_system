package com.example.pos_system.service;

import com.example.pos_system.dto.RegisterRequest;
import com.example.pos_system.dto.UpdateUserRequest;
import com.example.pos_system.model.Role;
import com.example.pos_system.model.User;
import com.example.pos_system.repository.RoleRepository;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        User creator = userRepository.findByUsername(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Creator user not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setCreatedBy(creator);

        userRepository.save(user);
        return "User registered successfully by " + creator.getUsername();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (!request.getUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getOldPassword() == null
                    || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
    }
}
