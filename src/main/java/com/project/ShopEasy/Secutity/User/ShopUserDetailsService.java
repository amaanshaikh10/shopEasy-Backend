package com.project.ShopEasy.Secutity.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Repository.UserRepository;

@Service
public class ShopUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = Optional.ofNullable(userRepository.findByEmail(email))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return ShopUserDetails.buildUserDetails(user);
	}

}
