package com.arsenr.yummy.role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByRoleNameReturnRole() {
        Role userRole = new Role(RoleName.USER);
        roleRepository.save(userRole);

        Optional<Role> role = roleRepository.findByRoleName(RoleName.USER);

        assertThat(role).isPresent();
        assertThat(role.get().getId()).isEqualTo(userRole.getId());
        assertThat(role.get().getRoleName()).isEqualTo(userRole.getRoleName());
    }

    @Test
    void testFindByRoleNameWhenRoleDoesNotExist() {
        RoleName moderatorRoleName = RoleName.MODERATOR;

        Optional<Role> role = roleRepository.findByRoleName(moderatorRoleName);

        assertThat(role).isEmpty();
    }

    @Test
    void testFindByRoleNameWhenRoleNameIsNull() {
        Optional<Role> role = roleRepository.findByRoleName(null);

        assertThat(role).isEmpty();
    }
}