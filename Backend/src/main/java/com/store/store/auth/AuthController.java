package com.store.store.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
    System.out.println(request);
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/getUserId/{username}") 
    public ResponseEntity<?> getUserId(@PathVariable String username) {
       Long userId = authService.getUserId(username);
       
       if(userId != null) {
        return ResponseEntity.ok(userId);
       }

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error al intentar obtener id mediante el username: "+username);
    } 

}
