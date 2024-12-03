package com.store.store.service;

import com.store.store.entity.CartEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartService {
    ResponseEntity<?>  getAllCarts();
    ResponseEntity<?> getCartById(@PathVariable Long id);
    ResponseEntity<?> getAllProductsOfUser(@PathVariable Long userId);
    ResponseEntity<?> updateAmountProduct(@RequestBody Long amount, @PathVariable Long cartId);
    Long getAmountFromProductOfUser(Long userId, Long productId);
    Long getCartId(Long userId, Long productId);
    ResponseEntity<?> addCart(@RequestBody CartEntity cartEntity);
    ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody CartEntity newCart);
    ResponseEntity<?> deleteCart(@PathVariable Long id);
    ResponseEntity<?> cleanCart(Long userId);
}
