package com.arsenr.yummy.auth;

import com.arsenr.yummy.exception.TokenException;
import com.arsenr.yummy.exception.UserRegistrationException;
import com.arsenr.yummy.handler.ErrorResponse;
import com.arsenr.yummy.jwt.JwtResponse;
import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.token.RefreshTokenRequest;
import com.arsenr.yummy.token.TokenValidationResponse;
import com.arsenr.yummy.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sInN1YiI6ImFyYWRtaW5AZ21haWwuY29tIiwiaWF0IjoxNzgxOTg3NzU2LCJleHAiOjE3ODE5ODg2NTZ9.0Xh2KBkU8GYXsgWylfO6r8Qt5T75qZKv2DaQcp5-0k0";
    private static final String ACCESS_TOKEN_NULLABLE = null;
    private static final String ACCESS_TOKEN_BLANKED = "";
    private static final String ACCESS_TOKEN_INVALID = "access.jwt.token";
    private static final String NEW_ACCESS_TOKEN = "new.access.token";

    private static final String REFRESH_TOKEN = "refresh.token.string";
    private static final String REFRESH_TOKEN_NULL = null;
    private static final String REFRESH_TOKEN_BLANKED = "";

    private static final String VALID_EMAIL = "johnpatterson1989@mail.com";
    private static final String INVALID_EMAIL = "johnpatterson1989mail.com";
    private static final String BLANKED_EMAIL = "";
    private static final String NULLABLE_EMAIL = null;

    private static final long EXPIRES_IN = 3_600_000L;

    @Test
    void testRegistrationShouldReturnSuccess() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl", "Arsen", "Rodyk", "", "ArsenRodyk", "ArsenRodyk");

        when(authService.signUp(signUpRequest)).thenReturn(JwtResponse.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .tokenType("Bearer")
                .expiresIn(EXPIRES_IN)
                .build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(EXPIRES_IN));

        Mockito.verify(authService).signUp(signUpRequest);
    }

    @Test
    void testRegistrationShouldReturnExceptionWhenUserExist() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl", "Arsen", "Rodyk", "", "ArsenRodyk", "ArsenRodyk");

        when(authService.signUp(signUpRequest)).thenThrow(new UserRegistrationException("Email already registered"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).signUp(signUpRequest);
    }

    @Test
    void testRegistrationShouldReturnExceptionWhenEmailIsNotValid() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("arsenrodykgmail.com", "GF9O4jw8r3Pl", "Arsen", "Rodyk", "", "ArsenRodyk", "ArsenRodyk");

        when(authService.signUp(signUpRequest)).thenThrow(new UserRegistrationException("Email is not valid"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signUp(signUpRequest);
    }

    @Test
    void testRegistrationShouldReturnExceptionWhenEmailIsBlank() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();

        when(authService.signUp(signUpRequest)).thenThrow(new UserRegistrationException("Email or password is blank"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signUp(signUpRequest);
    }

    @Test
    void testRegistrationShouldReturnExceptionWhenIsNullable() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(null, null, null, null, null, null, null);

        when(authService.signUp(signUpRequest)).thenThrow(new UserRegistrationException("This request is null"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signUp(signUpRequest);
    }

    @Test
    void testLoginShouldReturnSuccessWhenEmailIsExist() throws Exception {
        SignInRequest signInRequest = new SignInRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl");

        when(authService.signIn(signInRequest)).thenReturn(JwtResponse.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .tokenType("Bearer")
                .expiresIn(EXPIRES_IN).build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(EXPIRES_IN));

        Mockito.verify(authService).signIn(signInRequest);
    }

    @Test
    void testLoginShouldReturnExceptionWhenEmailIsNotExist() throws Exception {
        SignInRequest signInRequest = new SignInRequest("arsenrodyk@gmail.com", "GF9O4jw8r3Pl");

        when(authService.signIn(signInRequest)).thenThrow(new BadCredentialsException("Email not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).signIn(signInRequest);
    }

    @Test
    void testLoginShouldReturnExceptionWhenEmailIsNotValid() throws Exception {
        SignInRequest signInRequest = new SignInRequest("arsenrodykgmail.com", "GF9O4jw8r3Pl");

        when(authService.signIn(signInRequest)).thenThrow(new UsernameNotFoundException("Email not found"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signIn(signInRequest);
    }

    @Test
    void testLoginShouldReturnExceptionWhenEmailIsBlank() throws Exception {
        SignInRequest signInRequest = new SignInRequest();

        when(authService.signIn(signInRequest)).thenThrow(new UsernameNotFoundException("Email or password is blank"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signIn(signInRequest);
    }

    @Test
    void testLoginShouldReturnExceptionWhenEmailIsNullable() throws Exception {
        SignInRequest signInRequest = new SignInRequest(null, null);

        when(authService.signIn(signInRequest)).thenThrow(new UsernameNotFoundException("This request is null"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, never()).signIn(signInRequest);
    }

    @Test
    void testVerifyShouldReturnSuccessWhenTokenIsValid() throws Exception {
        TokenValidationResponse tokenValidationResponse = TokenValidationResponse.builder()
                .valid(true)
                .email("arsenrodyk@gmail.com")
                .roles(Set.of(new Role(RoleName.USER)))
                .expiresIn(EXPIRES_IN)
                .message("Token is valid")
                .build();

        when(authService.verifyToken(any())).thenReturn(tokenValidationResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenValidationResponse)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.email").value("arsenrodyk@gmail.com"))
                .andExpect(jsonPath("$.expiresIn").value(EXPIRES_IN))
                .andExpect(jsonPath("$.message").value(tokenValidationResponse.getMessage()));

        Mockito.verify(authService).verifyToken(any());
    }

    @Test
    void testVerifyShouldReturnExceptionWhenTokenIsNotValid() throws Exception {
        when(authService.verifyToken(any())).thenThrow(new MalformedJwtException("Access token is invalid"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN_INVALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, times(1)).verifyToken(any());
    }

    @Test
    void testVerifyShouldReturnExceptionWhenTokenIsExpired() throws Exception {
        when(authService.verifyToken(any())).thenThrow(new TokenException("Access token is expired"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, times(1)).verifyToken(any());
    }

    @Test
    void testVerifyShouldReturnExceptionWhenTokenIsNullable() throws Exception {
        when(authService.verifyToken(any())).thenThrow(new IllegalArgumentException("Access token is null"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN_NULLABLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, times(1)).verifyToken(any());
    }

    @Test
    void testVerifyShouldReturnExceptionWhenTokenIsBlank() throws Exception {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(IllegalArgumentException.class.getSimpleName())
                .message("Access token is blank")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .build();

        when(authService.verifyToken(any())).thenThrow(new IllegalArgumentException("Access token is blank"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN_BLANKED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorResponse)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService, times(1)).verifyToken(any());
    }

    @Test
    void testRotateRefreshTokenShouldReturnSuccessWhenTokenIsValid() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(REFRESH_TOKEN);
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(NEW_ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .tokenType("Bearer")
                .expiresIn(EXPIRES_IN)
                .build();
        when(authService.refreshToken(refreshTokenRequest)).thenReturn(jwtResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(NEW_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(EXPIRES_IN));

        Mockito.verify(authService).refreshToken(refreshTokenRequest);
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenTokenIsInvalid() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(REFRESH_TOKEN);

        when(authService.refreshToken(refreshTokenRequest)).thenThrow(new TokenException("Refresh token is invalid"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).refreshToken(refreshTokenRequest);
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenTokenIsExpired() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(REFRESH_TOKEN);

        when(authService.refreshToken(refreshTokenRequest)).thenThrow(new TokenException("Refresh token is expired"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).refreshToken(refreshTokenRequest);
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenTokenIsNullable() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(REFRESH_TOKEN_NULL);

        when(authService.refreshToken(refreshTokenRequest)).thenThrow(new IllegalArgumentException("Refresh token is null"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).refreshToken(refreshTokenRequest);
    }


    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenTokenIsBlank() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(REFRESH_TOKEN_BLANKED);

        when(authService.refreshToken(refreshTokenRequest)).thenThrow(new IllegalArgumentException("Refresh token is blank"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().is4xxClientError());

        Mockito.verify(authService).refreshToken(refreshTokenRequest);
    }

    @Test
    void testLogoutShouldReturnSuccessWhenEmailIsValid() throws Exception {
        User user = new User("Arsen", "Rodyk", "ArsenRodyk", "ArsenRodyk", VALID_EMAIL, "GF9O4jw8r3Pl", Set.of(new Role(RoleName.USER)));


        doNothing().when(authService).logout(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isNoContent());

        verify(authService).logout(user.getEmail());
    }

    @Test
    void testLogoutShouldReturnExceptionWhenEmailIsInvalid() throws Exception {
        User user = new User("Arsen", "Rodyk", "ArsenRodyk", "ArsenRodyk", INVALID_EMAIL, "GF9O4jw8r3Pl", Set.of(new Role(RoleName.USER)));


        doThrow(new UsernameNotFoundException("Email is invalid!")).when(authService).logout(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isNotFound());

        verify(authService).logout(user.getEmail());
    }

    @Test
    void testLogoutShouldReturnExceptionWhenEmailIsBlank() throws Exception {
        User user = new User("Arsen", "Rodyk", "ArsenRodyk", "ArsenRodyk", BLANKED_EMAIL, "GF9O4jw8r3Pl", Set.of(new Role(RoleName.USER)));

        doThrow(new IllegalArgumentException("Email is blanked!")).when(authService).logout(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isConflict());

        verify(authService).logout(user.getEmail());
    }

    @Test
    void testLogoutShouldReturnExceptionWhenEmailIsNull() throws Exception {
        User user = new User("Arsen", "Rodyk", "ArsenRodyk", "ArsenRodyk", NULLABLE_EMAIL, "GF9O4jw8r3Pl", Set.of(new Role(RoleName.USER)));


        doThrow(new IllegalArgumentException("Email is invalid!")).when(authService).logout(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isConflict());

        verify(authService).logout(user.getEmail());
    }
}
