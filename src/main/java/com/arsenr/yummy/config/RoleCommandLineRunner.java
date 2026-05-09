package com.arsenr.yummy.config;

import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class RoleCommandLineRunner implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleCommandLineRunner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() > 0) {
            log.info("Role Repository has already been initialized");
            return;
        }
        roleRepository.save(new Role(RoleName.USER));
        roleRepository.save(new Role(RoleName.ADMIN));
        roleRepository.save(new Role(RoleName.MODERATOR));
        log.info("Role Repository has been initialized");
    }
}
