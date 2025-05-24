package com.project.ShopEasy.Service.user;

import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.DTOs.UserDto;
import com.project.ShopEasy.Exception.AlreadyExistException;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Role;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Repository.RoleRepository;
import com.project.ShopEasy.Repository.UserRepository;
import com.project.ShopEasy.request.CreateUserRequest;
import com.project.ShopEasy.request.UserUpdateRequest;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
	}

	@Override
	public UserDto getUserByID(Long userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId).map(this::convertUserToDto)
				.orElseThrow(() -> new ResourceNotFoundException("User not found!"));
	}

	@Override
	public UserDto createUser(CreateUserRequest request) {
		// TODO Auto-generated method stub
		return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail())).map(req -> {
			User user = new User();
			user.setFirstName(req.getFirstName());
			user.setLastName(req.getLastName());
			user.setEmail(req.getEmail());
			user.setPassword(passwordEncoder.encode(req.getPassword()));
			Role userRole = roleRepository.findByName("ROLE_USER").get();
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			return convertUserToDto(user);
		}).orElseThrow(() -> new AlreadyExistException(request.getEmail() + "already exist!"));
	}

	@Override
	public UserDto updateUser(UserUpdateRequest request, Long userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId).map(exixtingUser -> {
			exixtingUser.setFirstName(request.getFirstName());
			exixtingUser.setLastName(request.getLastName());
			userRepository.save(exixtingUser);
			return convertUserToDto(exixtingUser);
		}).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
	}

	@Override
	public void deleteUser(Long userId) {
		// TODO Auto-generated method stub
		userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
			throw new ResourceNotFoundException("User not found!");
		});
	}

	private UserDto convertUserToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email);
	}
}
