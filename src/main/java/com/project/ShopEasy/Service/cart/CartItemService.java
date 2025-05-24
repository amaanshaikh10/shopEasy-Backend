package com.project.ShopEasy.Service.cart;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Model.CartItem;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.Repository.CartItemRepository;
import com.project.ShopEasy.Repository.CartRepository;
import com.project.ShopEasy.Service.product.IProductService;

@Service
public class CartItemService implements ICartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private IProductService productService;

	@Autowired
	private ICartService cartService;

	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		// TODO Auto-generated method stub

		Cart cart = cartService.getCart(cartId);
		Product product = productService.getProductById(productId);
		CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElse(new CartItem());
		if (cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUnitPrice(product.getPrice());
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);

	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		Cart cart = cartService.getCart(cartId);
		CartItem itemToRemove = getCartItem(cartId, productId);
		cart.removeItem(itemToRemove);
		cartItemRepository.delete(itemToRemove);
		if (cart.getItems().isEmpty()) {
			// Clear the cart instead of deleting it
			cart.getItems().clear();
			cart.setTotalAmount(BigDecimal.ZERO);
			cartRepository.save(cart);
		} else {
			cartRepository.save(cart); // Save the updated cart if not empty
		}

	}

	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		Cart cart = cartService.getCart(cartId);
		cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
				.ifPresent(item -> {
					item.setQuantity(quantity);
					item.setUnitPrice(item.getProduct().getPrice());
					item.setTotalPrice();
				});
		BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		cart.setTotalAmount(totalAmount);
		cartRepository.save(cart);
	}

	@Override
	public CartItem getCartItem(Long cartId, Long productId) {
		Cart cart = cartService.getCart(cartId);
		return cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Item not found"));
	}

}
