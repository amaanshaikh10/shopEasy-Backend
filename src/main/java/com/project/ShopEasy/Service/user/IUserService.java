package com.project.ShopEasy.Service.user;

import com.project.ShopEasy.DTOs.UserDto;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.request.CreateUserRequest;
import com.project.ShopEasy.request.UserUpdateRequest;

public interface IUserService {

	UserDto getUserByID(Long iserId);

	UserDto createUser(CreateUserRequest request);

	UserDto updateUser(UserUpdateRequest request, Long userId);

	void deleteUser(Long userId);

	User getUserById(Long userId);

	User getAuthenticatedUser();

}
