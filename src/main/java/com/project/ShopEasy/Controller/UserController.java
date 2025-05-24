package com.project.ShopEasy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.DTOs.UserDto;
import com.project.ShopEasy.Exception.AlreadyExistException;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.user.IOtpService;
import com.project.ShopEasy.Service.user.IUserService;
import com.project.ShopEasy.request.CreateUserRequest;
import com.project.ShopEasy.request.UserUpdateRequest;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IOtpService otpService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{userId}/user")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		try {
			UserDto user = userService.getUserByID(userId);

			return ResponseEntity.ok(new ApiResponse("User found!", user));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

//	@PostMapping("/register")
	private ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
		try {
			UserDto user = userService.createUser(request);
			return ResponseEntity.ok(new ApiResponse("Create Success!", user));
		} catch (AlreadyExistException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}

	}

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtp(@RequestBody CreateUserRequest request) {
		otpService.generateOtp(request.getEmail());
		System.out.println("🔍 sendOtp() called for: " + request.getEmail());
		return ResponseEntity.ok("OTP sent successfully.");
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse> verifyOtp(@RequestBody CreateUserRequest request, @RequestParam String otp) {
		if (otpService.verifyOtp(request.getEmail(), otp)) {
			return createUser(request);
		} else {
			return ResponseEntity.status(400).body(new ApiResponse("Invalid OTP.", null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
		try {
			UserDto user = userService.updateUser(request, userId);
			return ResponseEntity.ok(new ApiResponse("Update Success!", user));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("Delete Success!", null));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

	}

}
