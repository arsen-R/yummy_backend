package com.arsenr.yummy.auth;

import com.arsenr.yummy.token.RefreshTokenRequest;
import com.arsenr.yummy.token.TokenValidationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody SignUpRequest signUpRequest) {
        var result = authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest signInRequest) {
        var result = authService.signIn(signInRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/verify")
    public ResponseEntity<TokenValidationResponse> verify(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7) : authHeader;

        return ResponseEntity.ok(authService.verifyToken(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> rotateRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/valid")
    public ResponseEntity<TokenValidationResponse> isValid(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7) : authHeader;
        return ResponseEntity.ok(authService.verifyToken(token));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has been logged out");
    }
}
