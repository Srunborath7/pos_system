package com.example.pos_system.dto;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private String createdBy;
}
