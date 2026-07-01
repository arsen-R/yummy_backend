package com.arsenr.yummy.token;

import com.arsenr.yummy.exception.TokenException;
import com.arsenr.yummy.jwt.JwtService;
import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private Role role;
    private User user;
    private static final int EXPIRATION_TIME_MS = 1296000000;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", EXPIRATION_TIME_MS);

        role = new Role(RoleName.USER);

        user = new User("Arsen", "Rodyk", "ArsenRodyk", "ArsenRodyk", "arsenrodyk@gmail.com", "GF9O4jw8r3Pl", Set.of(role));
    }

    @Test
    void testCreateRefreshTokenShouldCreateRefreshToken() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken createdRefreshToken = refreshTokenService.createRefreshToken(user);

        assertNotNull(createdRefreshToken);
        assertNotNull(createdRefreshToken.getToken());
        assertFalse(createdRefreshToken.getToken().isBlank());
        assertNotNull(createdRefreshToken.getExpiryDate());
        assertFalse(createdRefreshToken.isExpired());
        assertNotNull(createdRefreshToken.getUser());
        assertFalse(createdRefreshToken.isRevoked());

        verify(refreshTokenRepository).save(any());
    }

    @Test
    void testCreateRefreshTokenShouldReturnExceptionWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.createRefreshToken(null));

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void testCreateRefreshTokenReturnsDistinctTokensForDifferentUsers() {
        User anotherUser = new User("Sean", "Espinoza", "SeanEspinoza", "SeanEspinoza", "seanespinoza1985@mail.com", "", Set.of(role));

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken createdRefreshTokenForUser = refreshTokenService.createRefreshToken(user);
        RefreshToken createdRefreshTokenForAnotherUser = refreshTokenService.createRefreshToken(anotherUser);

        assertNotNull(createdRefreshTokenForUser);
        assertNotNull(createdRefreshTokenForAnotherUser);
        assertEquals(user, createdRefreshTokenForUser.getUser());
        assertEquals(anotherUser, createdRefreshTokenForAnotherUser.getUser());
        assertNotEquals(createdRefreshTokenForUser.getToken(), createdRefreshTokenForAnotherUser.getToken());

        verify(refreshTokenRepository, times(2)).save(any());

    }

    @Test
    void testCreateRefreshTokenAlwaysProducesUniqueTokensAcrossManyRepeatedCalls() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int callCounter = 100;
        Set<String> uniqueTokens = new HashSet<>();

        for (int i = 0; i < callCounter; i++) {
            RefreshToken createdRefreshTokenForUser = refreshTokenService.createRefreshToken(user);
            uniqueTokens.add(createdRefreshTokenForUser.getToken());
        }

        assertSame(callCounter, uniqueTokens.size());
    }

    @Test
    void testCreateRefreshTokenUsesConfiguredExpirationValueNotAHardcodedOne() {
        long customExpirationMs = 60_000L; // 1 minute
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", customExpirationMs);

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Instant expirationDate = Instant.now();
        RefreshToken createdRefreshTokenForUser = refreshTokenService.createRefreshToken(user);

        assertThat(createdRefreshTokenForUser.getExpiryDate())
                .isAfter(expirationDate.plusMillis(customExpirationMs).minusSeconds(1))
                .isBefore(expirationDate.plusMillis(customExpirationMs).plusSeconds(5));
        assertThat(createdRefreshTokenForUser.getExpiryDate()).isBefore(expirationDate.plusMillis(EXPIRATION_TIME_MS));
    }

    @Test
    void testVerifyRefreshTokenShouldReturnRefreshTokenWhenIsVerified() {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), Instant.now().plusSeconds(3600), false, user);

        RefreshToken verifiedRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);

        assertNotNull(verifiedRefreshToken);
        assertEquals(refreshToken, verifiedRefreshToken);

        verify(refreshTokenRepository, never()).delete(refreshToken);
    }

    @Test
    void testVerifyRefreshTokenShouldReturnExceptionWhenIsNullable() {
        RefreshToken refreshToken = null;

        assertThrows(TokenException.class, () -> refreshTokenService.verifyRefreshToken(refreshToken));

        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    void testVerifyRefreshTokenShouldReturnExceptionWhenIsTokenExpired() {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), Instant.now().minusSeconds(3600), false, user);

        assertThrows(TokenException.class, () -> refreshTokenService.verifyRefreshToken(refreshToken));

        verify(refreshTokenRepository).delete(refreshToken);
    }


    @Test
    void testFindByTokenShouldReturnReturnRefreshTokenWhenIsValid() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, Instant.now(), false, user);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundedRefreshToken = refreshTokenService.findByToken(token);

        assertNotNull(foundedRefreshToken);
        assertEquals(refreshToken, foundedRefreshToken.get());

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void testFindByTokenShouldReturnReturnExceptionWhenNotExists() {
        String token = UUID.randomUUID().toString();

        when(refreshTokenRepository.findByToken(token)).thenThrow(TokenException.class);

        assertThrows(TokenException.class, () -> refreshTokenService.findByToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void testFindByTokenShouldReturnReturnExceptionWhenIsBlanked() {
        String token = "";

        when(refreshTokenRepository.findByToken(token)).thenThrow(TokenException.class);

        assertThrows(TokenException.class, () -> refreshTokenService.findByToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void testFindByTokenShouldReturnReturnExceptionWhenIsNullable() {
        String token = null;

        when(refreshTokenRepository.findByToken(token)).thenThrow(TokenException.class);

        assertThrows(TokenException.class, () -> refreshTokenService.findByToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void testFindByTokenShouldReturnReturnExceptionWhenRefreshTokenIsExpired() {
        String token = UUID.randomUUID().toString();

        RefreshToken expiredRefreshToken = new RefreshToken(token, Instant.now().minusSeconds(3600), false, user);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(expiredRefreshToken));

        Optional<RefreshToken> foundedRefreshToken = refreshTokenService.findByToken(token);

        assertNotNull(foundedRefreshToken);
        assertEquals(expiredRefreshToken, foundedRefreshToken.get());
        assertTrue(expiredRefreshToken.isExpired());

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void testRotateRefreshTokenShouldReturnRefreshTokenWhenOldIsValid() {
        String token = UUID.randomUUID().toString();
        RefreshToken oldRefreshToken = new RefreshToken(token, Instant.now().plusSeconds(3600), false, user);

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken rotatedRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken);

        assertNotNull(rotatedRefreshToken);
        assertNotEquals(oldRefreshToken, rotatedRefreshToken);
        assertFalse(rotatedRefreshToken.isExpired());

        verify(refreshTokenRepository).delete(oldRefreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testRotateRefreshTokenShouldReturnRefreshTokenWhenOldIsAlreadyExpired() {
        String token = UUID.randomUUID().toString();
        RefreshToken oldRefreshToken = new RefreshToken(token, Instant.now().minusSeconds(3600), false, user);

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken rotatedRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken);

        assertNotNull(rotatedRefreshToken);
        assertTrue(oldRefreshToken.isExpired());
        assertFalse(rotatedRefreshToken.isExpired());

        verify(refreshTokenRepository).delete(oldRefreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenOldIsNullable() {
        RefreshToken oldRefreshToken = null;

        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.rotateRefreshToken(oldRefreshToken));

        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    void testRotateRefreshTokenShouldReturnExceptionWhenOldTokenHasNoUser() {
        String token = UUID.randomUUID().toString();
        RefreshToken oldRefreshToken = new RefreshToken(token, Instant.now().plusSeconds(3600), false, null);

        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.rotateRefreshToken(oldRefreshToken));

        verify(refreshTokenRepository).delete(any(RefreshToken.class));
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void testRevokeAllUserTokensShouldCallDeleteByUser() {
        refreshTokenService.revokeAllUserTokens(user);

        verify(refreshTokenRepository).deleteByUser(user);
    }

    @Test
    void testRevokeAllUserTokensShouldPassNullToRepository_whenUserIsNull() {
        refreshTokenService.revokeAllUserTokens(null);

        verify(refreshTokenRepository).deleteByUser(null);
    }
}