package com.arsenr.yummy.user;

import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private User user;

private static final String EMAIL_DOES_NOT_EXIST = "doesnotexist@gmail.com";
private static final String INVALID_EMAIL = "doesnotexistgmail.com";
private static final String BLANKED_EMAIL = "";
private static final String NULLABLE_EMAIL = null;

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
    void testFindByEmailShouldReturnUser() {
        Optional<User> foundedUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundedUser.isPresent());
        assertThat(foundedUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(foundedUser.get().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(foundedUser.get().getLastName()).isEqualTo(user.getLastName());
        assertThat(foundedUser.get().getUserName()).isEqualTo(user.getUserName());
        assertThat(foundedUser.get().getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(foundedUser.get().getRoles().contains(role));
    }

    @Test
    void testFindByEmailWhenUserDoesNotExist() {
        Optional<User> foundedUser = userRepository.findByEmail(EMAIL_DOES_NOT_EXIST);
        assertThat(foundedUser).isEmpty();
    }

    @Test
    void testFindByEmailWhenEmailIsNull() {
        Optional<User> foundedUser = userRepository.findByEmail(NULLABLE_EMAIL);
        assertThat(foundedUser).isEmpty();
    }

    @Test
    void testFindByEmailWhenEmailIsInvalid() {
        Optional<User> foundedUser = userRepository.findByEmail(INVALID_EMAIL);
        assertThat(foundedUser).isEmpty();
    }

    @Test
    void testFindByEmailWhenEmailIsBlank() {
        Optional<User> foundedUser = userRepository.findByEmail(BLANKED_EMAIL);
        assertThat(foundedUser).isEmpty();
    }


    @Test
    void testExistsByEmailShouldReturnTrue() {
        boolean isUserExist = userRepository.existsByEmail(user.getEmail());
        assertThat(isUserExist).isTrue();
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenUserDoesNotExist() {
        boolean isUserExist = userRepository.existsByEmail(EMAIL_DOES_NOT_EXIST);
        assertThat(isUserExist).isFalse();
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailIsInvalid() {
        boolean isUserExist = userRepository.existsByEmail(INVALID_EMAIL);
        assertThat(isUserExist).isFalse();
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailIsNull() {
        boolean isUserExist = userRepository.existsByEmail(NULLABLE_EMAIL);
        assertThat(isUserExist).isFalse();
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailIsBlank() {
        boolean isUserExist = userRepository.existsByEmail(BLANKED_EMAIL);
        assertThat(isUserExist).isFalse();
    }
}