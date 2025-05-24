package com.project.ShopEasy.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.project.ShopEasy.Enum.OrderStatus;

public class OrderDto {

	private Long orderId;
	private LocalDate orderDate;
	private BigDecimal totalAmount;
	private OrderStatus orderStatus;
	private Set<OrderItemDto> orderItems = new HashSet<>();

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Set<OrderItemDto> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItemDto> orderItems) {
		this.orderItems = orderItems;
	}

}
