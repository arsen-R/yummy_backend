package com.arsenr.yummy.token;

import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private User user;

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

        roleRepository.save(role);
        userRepository.save(user);
    }

    @Test
    void testFindByTokenShouldReturnsRefreshToken() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, Instant.now(), false, user);
        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);

        assertTrue(optionalRefreshToken.isPresent());
        assertEquals(optionalRefreshToken.get().getId(), refreshToken.getId());
        assertEquals(optionalRefreshToken.get().getToken(), refreshToken.getToken());
        assertEquals(optionalRefreshToken.get().getExpiryDate(), refreshToken.getExpiryDate());
        assertEquals(optionalRefreshToken.get().getUser(), refreshToken.getUser());

    }

    @Test
    void testFindByTokenWhenTokenIsInvalid() {
        Optional<RefreshToken> invalidRefreshToken = refreshTokenRepository.findByToken("this-token-does-not-exist");

        assertTrue(invalidRefreshToken.isEmpty());
    }

    @Test
    void testFindByTokenWhenTokenIsNullable() {
        Optional<RefreshToken> nullableRefreshToken = refreshTokenRepository.findByToken(null);

        assertTrue(nullableRefreshToken.isEmpty());
    }

    @Test
    void testFindByTokenWhenTokenBlanked() {
        Optional<RefreshToken> blankedRefreshToken = refreshTokenRepository.findByToken("");

        assertTrue(blankedRefreshToken.isEmpty());


    }

    @Test
    void testFindByTokenWhenTokenIsExpired() {
        String token = UUID.randomUUID().toString();
        RefreshToken expiredToken = new RefreshToken(token, Instant.now().minusSeconds(3600), false, user);
        refreshTokenRepository.save(expiredToken);

        Optional<RefreshToken> foundedToken = refreshTokenRepository.findByToken(token);

        assertTrue(foundedToken.isPresent());
        assertTrue(foundedToken.get().isExpired());
    }

    @Test
    void testDeleteByUser() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, Instant.now(), false, user);
        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.deleteByUser(refreshToken.getUser());

        Optional<RefreshToken> foundedToken = refreshTokenRepository.findByToken(token);

        assertTrue(foundedToken.isEmpty());
    }

    @Test
    void testDeleteByUserWhenUserIsNull() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, Instant.now(), false, user);
        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.deleteByUser(null);

        Optional<RefreshToken> foundedToken = refreshTokenRepository.findByToken(token);

        assertTrue(foundedToken.isPresent());
    }
}