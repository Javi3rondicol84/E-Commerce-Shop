package com.store.store.entity.dto;

import com.store.store.entity.ProductEntity;
import com.store.store.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDto {
    private Long cartId;
    private ProductEntity product;
    private User user;
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CartDto(Long cartId, ProductEntity product, User user, Long amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.cartId = cartId;
        this.product = product;
        this.user = user;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
