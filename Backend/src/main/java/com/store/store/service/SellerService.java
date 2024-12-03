package com.store.store.service;

import org.springframework.http.ResponseEntity;

public interface SellerService {
    ResponseEntity<?> convertToSeller(Long userId);
}
