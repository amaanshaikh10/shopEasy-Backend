package com.project.ShopEasy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Response.JwtResponse;
import com.project.ShopEasy.Secutity.User.ShopUserDetails;
import com.project.ShopEasy.Secutity.jwt.JwtUtils;
import com.project.ShopEasy.request.LoginRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateTokenForUser(authentication);
			ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
			return ResponseEntity.ok(new ApiResponse("Login Success!", jwtResponse));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));

		}
	}
}
