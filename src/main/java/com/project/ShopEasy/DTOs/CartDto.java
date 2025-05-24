package com.project.ShopEasy.DTOs;

import java.math.BigDecimal;
import java.util.Set;

public class CartDto {

	private Long cartId;
	private Set<CartItemDto> items;
	private BigDecimal totalAmount;

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Set<CartItemDto> getItems() {
		return items;
	}

	public void setItems(Set<CartItemDto> items) {
		this.items = items;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
