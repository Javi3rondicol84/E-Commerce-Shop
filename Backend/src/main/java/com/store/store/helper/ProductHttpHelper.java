package com.store.store.helper;

import com.store.store.entity.ProductEntity;
import com.store.store.entity.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ProductHttpHelper {

    public ResponseEntity<?> getByCategoryResponse(List<ProductDto> products, String category) {
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo encontrar ningun producto con la categoria " + category);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(products);
    }


    public ResponseEntity<?> getAllCategoriesResponse(List<String> products) {
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo encontrar ninguna categoria");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(products);
    }

    public ResponseEntity<?> getProductsByProductNameResponse(List<ProductDto> products, String keyword) {
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo encontrar ningun producto");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(products);
    }

    public ResponseEntity<?> getProductsPaginatedResponse(List<ProductEntity> products, String category, Long limit, Long offset) {
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo encontrar ningun producto");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(products);
    }

}
