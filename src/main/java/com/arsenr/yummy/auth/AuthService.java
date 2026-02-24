package com.arsenr.yummy.auth;

import com.arsenr.yummy.payload.request.SignInRequest;
import com.arsenr.yummy.payload.request.SignUpRequest;
import com.arsenr.yummy.payload.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    JwtResponse signUp(SignUpRequest signUpRequest);
    JwtResponse signIn(SignInRequest signInRequest);
}
