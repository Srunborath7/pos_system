package com.example.pos_system.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String categoryName;
    private String createdBy;
}
