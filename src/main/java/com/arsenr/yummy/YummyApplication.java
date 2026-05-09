package com.arsenr.yummy;

import com.arsenr.yummy.role.Role;
import com.arsenr.yummy.role.RoleName;
import com.arsenr.yummy.role.RoleRepository;
import com.arsenr.yummy.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YummyApplication {

	public static void main(String[] args) {
		SpringApplication.run(YummyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByRoleName(RoleName.USER).isEmpty()) {
				Role role = new Role();
				role.setRoleName(RoleName.USER);
				roleRepository.save(role);
			}
			if (roleRepository.findByRoleName(RoleName.ADMIN).isEmpty()) {
				Role role = new Role();
				role.setRoleName(RoleName.ADMIN);
				roleRepository.save(role);
			}
		};
	}

}
