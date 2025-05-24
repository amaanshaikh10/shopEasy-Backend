package com.project.ShopEasy.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.cart.ICartService;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

	@Autowired
	private ICartService cartService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{cartId}/my-cart")
	public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
		try {
			Cart cart = cartService.getCart(cartId);
			return ResponseEntity.ok(new ApiResponse("Cart found", cart));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), cartId));
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/{cartId}/clear")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
		try {
			cartService.clearCart(cartId);
			return ResponseEntity.ok(new ApiResponse("Cleared success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), cartId));
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{cartId}/cart/total-price")
	public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long cartId) {
		try {
			BigDecimal totalPrice = cartService.getTotalPrice(cartId);
			return ResponseEntity.ok(new ApiResponse("Success!", totalPrice));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), cartId));
		}
	}

}
