package com.project.ShopEasy.DTOs;

import java.util.List;

public class UserDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private CartDto cart;
	private List<OrderDto> orders;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CartDto getCart() {
		return cart;
	}

	public void setCart(CartDto cart) {
		this.cart = cart;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}

}
