package com.store.store.auth;

import com.store.store.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    public void setRole(String roleToAdd) {
        this.role = roleToAdd.toUpperCase();
    }
}
