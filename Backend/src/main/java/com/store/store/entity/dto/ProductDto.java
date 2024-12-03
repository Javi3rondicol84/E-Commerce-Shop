package com.store.store.entity.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long productId;
    private String productName;
    private String description;
    private String category;
    private Double price;
    private int stock;

    public ProductDto(Long productId, String productName, String description, String category, Double price, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
}
