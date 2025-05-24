package com.project.ShopEasy.Service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Repository.CartItemRepository;
import com.project.ShopEasy.Repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService implements ICartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	private AtomicLong cartIdGenerator = new AtomicLong(0);

//	private static final Logger log = LoggerFactory.getLogger(CartService.class);

	@Override
	public Cart getCart(Long id) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		BigDecimal totalAmount = cart.getTotalAmount();
		cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}

	@Transactional
	@Override
	public void clearCart(Long id) {
		// TODO Auto-generated method stub
		Cart cart = getCart(id);
		cartItemRepository.deleteAllByCartId(id);
		cart.clearCart();
		cartRepository.deleteById(id);

	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCart(id);
		return cart.getTotalAmount();
	}

	@Override
	public Cart initializeNewCart(User user) {
		return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() -> {
			Cart cart = new Cart();
			cart.setUser(user);
			return cartRepository.save(cart);
		});
	}

	@Override
	public Cart getOrInitializeCart(Long cartId) {
		if (cartId != null) {
			return getCart(cartId); // Reuse existing cart
		}

		// Check if there's any existing empty cart
		Cart emptyCart = cartRepository.findAll().stream().filter(cart -> cart.getItems().isEmpty()).findFirst()
				.orElse(null);

		if (emptyCart != null) {
			return emptyCart; // Reuse the empty cart
		}

		// Create a new cart if no empty cart exists
		Cart newCart = new Cart();
		cartRepository.save(newCart);
		return newCart;
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		// TODO Auto-generated method stub

		return cartRepository.findByUserId(userId);
	}

}
