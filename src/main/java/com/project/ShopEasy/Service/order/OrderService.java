package com.project.ShopEasy.Service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ShopEasy.DTOs.OrderDto;
import com.project.ShopEasy.Enum.OrderStatus;
import com.project.ShopEasy.Exception.ResourceNotFoundException;
import com.project.ShopEasy.Model.Cart;
import com.project.ShopEasy.Model.Order;
import com.project.ShopEasy.Model.OrderItem;
import com.project.ShopEasy.Model.Product;
import com.project.ShopEasy.Model.User;
import com.project.ShopEasy.Repository.OrderRepository;
import com.project.ShopEasy.Repository.ProductRepository;
import com.project.ShopEasy.Service.cart.ICartService;
import com.project.ShopEasy.Service.invoice.InvoiceGenerator;
import com.project.ShopEasy.Service.user.IOtpService;
import com.project.ShopEasy.Service.user.IUserService;

@Service
public class OrderService implements IOrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ICartService cartService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IOtpService otpService;

	@Autowired
	private IUserService userService;

	@Autowired
	private InvoiceGenerator generator;

	@Override
	@Transactional
	public OrderDto placeOrder(Long userId) {
		// TODO Auto-generated method stub
		Cart cart = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItems = createOrderItems(order, cart);

		if (orderItems.isEmpty()) {
			throw new IllegalStateException("Cart is empty! Cannot place order.");
		}

		order.setOrderItems(new HashSet<>(orderItems));
		order.setTotalAmount(calculateTotalAmount(orderItems));

		Order savedOrder = orderRepo.save(order);
		cartService.clearCart(cart.getId());

		User user = userService.getUserById(userId);

		otpService.sendOrderConformation(user.getEmail(), savedOrder.getId());
		generator.processOrder(user.getFirstName(), user.getEmail(), orderItems);

		return convertToDto(savedOrder);
	}

	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return order;
	}

	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getItems().stream().map(cartItems -> {
			Product product = productRepository.findById(cartItems.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

			if (product.getInventory() < cartItems.getQuantity()) {
				throw new IllegalStateException("Not enough stock for product: " + product.getName());
			}

			product.setInventory(product.getInventory() - cartItems.getQuantity());
			productRepository.save(product);
			return new OrderItem(order, product, cartItems.getQuantity(), cartItems.getUnitPrice());
		}).toList();
	}

	private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
		return orderItemList.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		// TODO Auto-generated method stub
		return orderRepo.findById(orderId).map(this::convertToDto)
				.orElseThrow(() -> new ResourceNotFoundException("Order not exist"));
	}

	@Override
	public List<OrderDto> getUserOrders(Long userId) {
		List<Order> orders = orderRepo.findByUserId(userId);
		return orders.stream().map(this::convertToDto).toList();
	}

	private OrderDto convertToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}

}
