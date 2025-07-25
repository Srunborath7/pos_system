package com.example.pos_system.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
