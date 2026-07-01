package com.arsenr.yummy.token;

import com.arsenr.yummy.exception.TokenException;
import com.arsenr.yummy.jwt.JwtService;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setRevoked(false);
        refreshToken.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(RefreshToken refreshToken) {
        if (refreshToken == null) {
            throw new TokenException("Token is null");
        }
        if(refreshToken.isExpired() ){
            refreshTokenRepository.delete(refreshToken);
            throw new TokenException("Refresh token was expired. Please make a new authentication request");
        }
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenResponse generateNewJwtToken(RefreshTokenRequest refreshTokenRequest) {
        User user = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .map(this::verifyRefreshToken)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException("Refresh token does not exist"));
        String token = jwtService.generateToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .tokenType("BEARER")
                .build();
    }

    @Transactional
    @Override
    public RefreshToken rotateRefreshToken(RefreshToken old) {
        if (old == null) {
            throw new IllegalArgumentException("Refresh token must not be null");
        }
        refreshTokenRepository.delete(old);
        return createRefreshToken(old.getUser());
    }

    @Transactional
    @Override
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
