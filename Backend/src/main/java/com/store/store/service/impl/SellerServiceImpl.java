package com.store.store.service.impl;

import com.store.store.entity.SellerEntity;
import com.store.store.repository.SellerRepository;
import com.store.store.service.SellerService;
import com.store.store.user.Role;
import com.store.store.user.User;
import com.store.store.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> convertToSeller(Long userId) {
        User user = userRepository.findById(userId).get();

        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user with user id: "+userId);
        }

        user.setRole(Role.SELLER);
        userRepository.save(user);
        SellerEntity seller = sellerRepository.getSellerByUser(user);

        if(seller == null) {
            seller = new SellerEntity(user);
            sellerRepository.save(seller);
        }

        return ResponseEntity.ok("exitoso");
    }

}
