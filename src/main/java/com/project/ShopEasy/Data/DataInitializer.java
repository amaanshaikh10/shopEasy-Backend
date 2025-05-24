package com.project.ShopEasy.Data;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.ShopEasy.Model.Role;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Repository.RoleRepository;
import com.project.ShopEasy.Repository.UserRepository;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		createDefaultRoleIfNotExits(defaultRoles);
		createDefaultUserIfNotExits();
		createDefaultAdminIfNotExits();
	}

	private void createDefaultUserIfNotExits() {
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("User role not found"));
		for (int i = 1; i <= 5; i++) {
			String defaultEmail = "user" + i + "@email.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("The User");
			user.setLastName("User" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default vet user " + i + " created successfully.");
		}
	}

	private void createDefaultAdminIfNotExits() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
		for (int i = 1; i <= 2; i++) {
			String defaultEmail = "admin" + i + "@email.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("Admin" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Default admin user " + i + " created successfully.");
		}
	}

	private void createDefaultRoleIfNotExits(Set<String> roles) {
		roles.stream().filter(role -> roleRepository.findByName(role).isEmpty())
				.forEach(role -> roleRepository.save(new Role(role)));

	}
}
