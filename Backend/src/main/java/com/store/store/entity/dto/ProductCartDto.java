package com.store.store.entity.dto;

import com.store.store.entity.ProductEntity;

public class ProductCartDto {
    private ProductEntity product;
    private Long amount;
    private Long cartId;

    public ProductCartDto(ProductEntity product, Long amount, Long cartId) {
        this.product = product;
        this.amount = amount;
        this.cartId = cartId;
    }

    public ProductCartDto(ProductEntity product, Long amount) {
        this.product = product;
        this.amount = amount;
    }
    public ProductEntity getProduct() {
        return product;
    }
    public void setProduct(ProductEntity product) {
        this.product = product;
    }
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public Long getCartId() {
        return cartId;
    }
    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    
}
