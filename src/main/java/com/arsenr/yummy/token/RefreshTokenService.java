package com.arsenr.yummy.token;

import com.arsenr.yummy.user.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewJwtToken(RefreshTokenRequest refreshTokenRequest);
    RefreshToken rotateRefreshToken(RefreshToken old);
    void revokeAllUserTokens(User user);

//    ResponseCookie generateResponseCookie(String token);
//    String getRefreshTokenFromCookies(HttpServletRequest request);
//    void deleteByToken(String token);
//    ResponseCookie getCleanRefreshTokenCookie();
}
