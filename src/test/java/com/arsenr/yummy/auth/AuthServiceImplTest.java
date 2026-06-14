package com.arsenr.yummy.auth;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImplTest")
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;

    private User user;
    private Role role;
    private RefreshToken refreshToken;
    private RefreshToken newRefreshToken;


    private static final String ACCESS_TOKEN = "access.jwt.token";
    private static final String REFRESH_TOKEN = "refresh.token.string";
    private static final String NEW_REFRESH_TOKEN = "new.refresh.token.string";

    private static final long EXPIRES_IN = 3_600_000L;


    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(RoleName.USER);

        user = new User();
        user.setFirstName("Arsen");
        user.setLastName("Rodyk");
        user.setUserName("ArsenRodyk");
        user.setDisplayName("ArsenRodyk");
        user.setEmail("arsenrodyk@gmail.com");
        user.setPassword("GF9O4jw8r3Pl");
        user.setRoles(Set.of(role));


        refreshToken = new RefreshToken();
        refreshToken.setToken(REFRESH_TOKEN);
        refreshToken.setUser(user);

        newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(NEW_REFRESH_TOKEN);
        newRefreshToken.setUser(user);
    }

    @Test
    void testSignUpShouldReturnJwtResponseWhenUserNotExists() {
        SignUpRequest signUpRequest = new SignUpRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl", "Arsen", "Rodyk", "", "ArsenRodyk", "ArsenRodyk");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(signUpRequest.getPassword())).thenReturn("GF9O4jw8r3Pl");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(ACCESS_TOKEN);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);
        when(jwtService.getExpirationMillis(ACCESS_TOKEN)).thenReturn(EXPIRES_IN);


        JwtResponse response = authServiceImpl.signUp(signUpRequest);

        assertNotNull(response);
        assertThat(response.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(response.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(EXPIRES_IN);

        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        verify(refreshTokenService).createRefreshToken(any(User.class));
    }

    @Test
    void testSignUpShouldReturnExceptionWhenUserExists() {
        SignUpRequest signUpRequest = new SignUpRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl", "Arsen", "Rodyk", "", "ArsenRodyk", "ArsenRodyk");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        assertThrows(Exception.class, () -> authServiceImpl.signUp(signUpRequest));

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testAuthenticationShouldReturnJwtResponseWhenUserExists() {
        SignInRequest signInRequest = new SignInRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl");

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(User.class))).thenReturn(ACCESS_TOKEN);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);

        JwtResponse response = authServiceImpl.signIn(signInRequest);

        assertNotNull(response);
        assertThat(response.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(response.getRefreshToken()).isEqualTo(REFRESH_TOKEN);

        verify(jwtService).generateToken(any(User.class));
        verify(refreshTokenService).createRefreshToken(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticationShouldReturnExceptionWhenUserNotExists() {
        SignInRequest signInRequest = new SignInRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl");

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);

        assertThrows(Exception.class, () -> authServiceImpl.signIn(signInRequest));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRotateRefreshTokenShouldReturnJwtResponseWhenUserExists() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken.getToken());

        when(refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyRefreshToken(refreshToken)).thenReturn(refreshToken);
        when(refreshTokenService.rotateRefreshToken(refreshToken)).thenReturn(newRefreshToken);
        when(jwtService.generateToken(user)).thenReturn(ACCESS_TOKEN);
        when(jwtService.getExpirationMillis(ACCESS_TOKEN)).thenReturn(EXPIRES_IN);

        JwtResponse response = authServiceImpl.refreshToken(refreshTokenRequest);

        assertNotNull(response);
        assertThat(response.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(EXPIRES_IN);

        verify(refreshTokenService).findByToken(refreshTokenRequest.getRefreshToken());
        verify(refreshTokenService).verifyRefreshToken(refreshToken);
        verify(refreshTokenService).rotateRefreshToken(refreshToken);
        verify(jwtService).generateToken(user);
        verify(jwtService).getExpirationMillis(ACCESS_TOKEN);
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenTokenNotExists() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken.getToken());

        when(refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authServiceImpl.refreshToken(refreshTokenRequest));

        verify(refreshTokenService).findByToken(refreshTokenRequest.getRefreshToken());
    }


    @Test
    void testVerifyTokenShouldReturnTokenValidationResponseWhenTokenIsValid() {
        when(jwtService.isTokenStructurallyValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtService.extractUsername(ACCESS_TOKEN)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.getExpirationMillis(ACCESS_TOKEN)).thenReturn(EXPIRES_IN);

        TokenValidationResponse response = authServiceImpl.verifyToken(ACCESS_TOKEN);

        assertNotNull(response);
        assertThat(response.isValid()).isTrue();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getExpiresIn()).isEqualTo(EXPIRES_IN);
        assertThat(response.getMessage()).isEqualTo("Token is valid");

        verify(jwtService).isTokenStructurallyValid(ACCESS_TOKEN);
        verify(jwtService).extractUsername(ACCESS_TOKEN);
        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtService).getExpirationMillis(ACCESS_TOKEN);
    }
    @Test
    void verifyTokenShouldReturnInvalid_whenTokenStructureIsBad() {
        when(jwtService.isTokenStructurallyValid(ACCESS_TOKEN)).thenReturn(false);

        TokenValidationResponse response = authServiceImpl.verifyToken(ACCESS_TOKEN);

        assertThat(response.isValid()).isFalse();
        assertThat(response.getExpiresIn()).isEqualTo(-1);
        assertThat(response.getMessage()).isEqualTo("Token is invalid or expired");

        // nothing else should be called after structural check fails
        verify(jwtService, never()).extractUsername(any());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void testVerifyTokenShouldReturnInvalidWhenUserNotFound() {
        when(jwtService.isTokenStructurallyValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtService.extractUsername(ACCESS_TOKEN)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        TokenValidationResponse response = authServiceImpl.verifyToken(ACCESS_TOKEN);

        assertThat(response.isValid()).isFalse();
        assertThat(response.getExpiresIn()).isEqualTo(-1);
        assertThat(response.getMessage()).isEqualTo("User not found");

        verify(jwtService, never()).getExpirationMillis(any());
    }

    @Test
    void testVerifyTokenShouldStripBearerPrefixBeforeValidating() {
        String bearerToken = "Bearer " + ACCESS_TOKEN;

        when(jwtService.isTokenStructurallyValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtService.extractUsername(ACCESS_TOKEN)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.getExpirationMillis(ACCESS_TOKEN)).thenReturn(EXPIRES_IN);

        TokenValidationResponse response = authServiceImpl.verifyToken(bearerToken);

        assertThat(response.isValid()).isTrue();
        verify(jwtService).isTokenStructurallyValid(ACCESS_TOKEN);
    }
}