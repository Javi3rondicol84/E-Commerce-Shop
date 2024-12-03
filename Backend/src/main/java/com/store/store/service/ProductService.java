package com.store.store.service;

import com.store.store.entity.ProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProductService {
    ResponseEntity<?> getAllProducts();
    ResponseEntity<?> getProductById(@PathVariable Long id);
    ResponseEntity<?> createProduct(@RequestBody ProductEntity product);
    ResponseEntity<?> updateProduct(@RequestBody ProductEntity newProduct, @PathVariable Long id);
    ResponseEntity<?> deleteProduct(@PathVariable Long id);
    ResponseEntity<?> getAllCategories();
    ResponseEntity<?> getByCategory(@RequestParam String category);
    ResponseEntity<?> getProductByProductName(@RequestParam String keyword);
    ResponseEntity<?> getProductsPaginated(@RequestParam String category, @RequestParam Long limit, @RequestParam Long offset);


}
