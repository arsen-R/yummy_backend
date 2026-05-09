package com.arsenr.yummy.config;

import com.arsenr.yummy.exception.RoleNotFoundException;
import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@Order(2)
@Slf4j
public class UserCommandLineRunner implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserCommandLineRunner(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("User Repository has already been initialized");
            return;
        }
        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RoleNotFoundException("Admin Role not found"));

        User adminUser = new User(
                "Arsen",
                "Rodyk",
                "Admin#1",
                "Admin#1",
                "aradmin@gmail.com",
                bCryptPasswordEncoder.encode("Admin#124"),
                Set.of(adminRole)
        );
        userRepository.save(adminUser);
        log.info("User has been initialized");
    }
}
