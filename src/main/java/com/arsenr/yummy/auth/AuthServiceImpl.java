package com.arsenr.yummy.auth;

import com.arsenr.yummy.exception.UserRegistrationException;
import com.arsenr.yummy.jwt.JwtService;
import com.arsenr.yummy.payload.request.SignInRequest;
import com.arsenr.yummy.payload.request.SignUpRequest;
import com.arsenr.yummy.payload.response.JwtResponse;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new JwtResponse(jwtToken, refreshToken);
    }

    @Override
    public JwtResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new JwtResponse(jwtToken, refreshToken);
    }
}
