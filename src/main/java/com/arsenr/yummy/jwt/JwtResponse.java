package com.arsenr.yummy.jwt;

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
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;     // "Bearer"
    private long   expiresIn;     // seconds until access token expires
}
