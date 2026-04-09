package com.arsenr.yummy.auth;

import com.arsenr.yummy.exception.TokenException;
import com.arsenr.yummy.exception.UserRegistrationException;
import com.arsenr.yummy.jwt.JwtResponse;
import com.arsenr.yummy.jwt.JwtService;
import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import com.arsenr.yummy.token.RefreshToken;
import com.arsenr.yummy.token.RefreshTokenRequest;
import com.arsenr.yummy.token.RefreshTokenService;
import com.arsenr.yummy.token.TokenValidationResponse;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;


    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           RoleRepository roleRepository,
                           RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public JwtResponse signUp(SignUpRequest signUpRequest) {
        boolean isUserExist = userRepository.existsByEmail(signUpRequest.email());
        if (isUserExist) {
            throw new UserRegistrationException("Invalid Registration Request");
        }

        User user = new User();
        user.setEmail(signUpRequest.email().toLowerCase().trim());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.password().trim()));
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setDisplayName(signUpRequest.displayName());
        user.setBio(signUpRequest.bio());
        user.setUserName(signUpRequest.username());

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Default role not configured"));
        roles.add(role);

        user.setRole(roles);
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);
        return JwtResponse.builder().accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMillis(jwtToken))
                .build();
    }

    @Override
    public JwtResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return JwtResponse.builder().accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMillis(jwtToken))
                .build();
    }

    @Transactional
    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new TokenException("Refresh token does not exist"));
        refreshTokenService.verifyRefreshToken(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);
        User user = newRefreshToken.getUser();

        String accessToken = jwtService.generateToken(user);
        return JwtResponse.builder().accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMillis(accessToken))
                .build();
    }

    @Override
    public TokenValidationResponse verifyToken(String token) {
        // Strip "Bearer " prefix if caller included it
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtService.isTokenStructurallyValid(token)) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .expiresIn(-1)
                    .message("Token is invalid or expired")
                    .build();
        }

        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .expiresIn(-1)
                    .message("User not found")
                    .build();
        }

        return TokenValidationResponse.builder()
                .valid(true)
                .email(email)
                .roles(user.getRole())
                .expiresIn(jwtService.getExpirationMillis(token))
                .message("Token is valid")
                .build();
    }

    public void logout(String email) {
        userRepository.findByEmail(email)
                .ifPresent(refreshTokenService::revokeAllUserTokens);
    }
}
