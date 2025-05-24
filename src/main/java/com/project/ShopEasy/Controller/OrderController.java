package com.project.ShopEasy.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ShopEasy.DTOs.OrderDto;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Response.ApiResponse;
import com.project.ShopEasy.Service.order.IOrderService;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
		try {
			OrderDto order = orderService.placeOrder(userId);
			return ResponseEntity.ok(new ApiResponse("Order Success!", order));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
		try {
			OrderDto order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Success!", order));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
		}
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<ApiResponse> getUserOrder(@PathVariable Long userId) {
		try {
			List<OrderDto> orders = orderService.getUserOrders(userId);
			return ResponseEntity.ok(new ApiResponse("Success!", orders));
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
		}
	}

}
