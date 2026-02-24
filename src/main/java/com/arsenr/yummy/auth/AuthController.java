package com.arsenr.yummy.auth;

import com.arsenr.yummy.payload.request.SignInRequest;
import com.arsenr.yummy.payload.request.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        var result = authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        var result = authService.signIn(signInRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
