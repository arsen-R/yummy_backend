package com.arsenr.yummy.auth;

import com.arsenr.yummy.jwt.JwtResponse;
import com.arsenr.yummy.token.RefreshTokenRequest;
import com.arsenr.yummy.token.TokenValidationResponse;

public interface AuthService {
    JwtResponse signUp(SignUpRequest signUpRequest);
    JwtResponse signIn(SignInRequest signInRequest);
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    TokenValidationResponse verifyToken(String token);
    void logout(String email);
}
