package com.arsenr.yummy.token;

import com.arsenr.yummy.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String email;
    private Set<Role> roles;
    private long expiresIn;
    private String message;
}