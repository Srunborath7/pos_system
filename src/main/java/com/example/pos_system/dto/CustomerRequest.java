package com.example.pos_system.dto;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
}
