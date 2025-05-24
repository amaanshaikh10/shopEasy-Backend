package com.project.ShopEasy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.cart.ICartItemService;
import com.project.ShopEasy.Service.cart.ICartService;
import com.project.ShopEasy.Service.user.IUserService;

import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

	@Autowired
	private ICartItemService cartItemService;

	@Autowired
	private ICartService cartService;

	@Autowired
	private IUserService userService;

	@PostMapping("/item/add")
	public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
		try {
			User user = userService.getAuthenticatedUser();
			Cart cart = cartService.initializeNewCart(user);

			cartItemService.addItemToCart(cart.getId(), productId, quantity);
			return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		} catch (JwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
		try {
			cartItemService.removeItemFromCart(cartId, itemId);
			return ResponseEntity.ok(new ApiResponse("Remove item success!", null));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/cart/{cartId}/item/{productId}/update")
	public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId,
			@RequestParam int quantity) {
		try {
			cartItemService.updateItemQuantity(cartId, productId, quantity);
			return ResponseEntity.ok(new ApiResponse("Update success!", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

	}

}
