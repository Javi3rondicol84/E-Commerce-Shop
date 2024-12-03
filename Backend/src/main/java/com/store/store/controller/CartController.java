package com.store.store.controller;
import com.store.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.store.store.entity.CartEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart/")
    public ResponseEntity<?> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @GetMapping("/cart/products/{userId}")
    public ResponseEntity<?> getAllProductsOfUser(@PathVariable Long userId) {
        return cartService.getAllProductsOfUser(userId);
    }

   @PutMapping("/cart/updateAmount/{cartId}")
    public ResponseEntity<?> updateAmountProduct(@RequestBody Long amount, @PathVariable Long cartId) {
        return cartService.updateAmountProduct(amount, cartId);
    }

    private Long getAmountFromProductOfUser(Long userId, Long productId) {
        return cartService.getAmountFromProductOfUser(userId, productId);
    }

    @GetMapping("cart/getCartIdFromUser/{userId}/{productId}")
    private Long getCartId(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.getCartId(userId, productId);
    }


    @PostMapping("/cart/add")
    public ResponseEntity<?> addCart(@RequestBody CartEntity cartEntity) {
        return cartService.addCart(cartEntity);
    }

   @PutMapping("cart/update/{id}")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody CartEntity newCart) {
        return cartService.updateCart(id, newCart);
    }

    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        return cartService.deleteCart(id);
    }

    @DeleteMapping("/cart/cleancart/{userId}")
    public ResponseEntity<?> cleanCart(@PathVariable Long userId) {
        return cartService.cleanCart(userId);
    }


}