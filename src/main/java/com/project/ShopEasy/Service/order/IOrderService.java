package com.project.ShopEasy.Service.order;

import java.util.List;

import com.project.ShopEasy.DTOs.OrderDto;

public interface IOrderService {

	OrderDto placeOrder(Long userId);

	OrderDto getOrder(Long orderId);

	List<OrderDto> getUserOrders(Long userId);
}
