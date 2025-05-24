package com.project.ShopEasy.Service.cart;

import java.math.BigDecimal;

import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Model.User;

public interface ICartService {

	Cart getCart(Long id);

	void clearCart(Long id);

	BigDecimal getTotalPrice(Long id);

	Cart getOrInitializeCart(Long cartId);

	Cart getCartByUserId(Long userId);

	Cart initializeNewCart(User user);
}
