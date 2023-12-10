package com.finalproject.storemanagementproject.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Branch;
import com.finalproject.storemanagementproject.models.Customer;
import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.OrderProduct;
import com.finalproject.storemanagementproject.models.Status;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.repositories.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	public Order createOrder(Branch branch, User user, Customer customer, List<OrderProduct> orderProducts) {
		Order createdOrder = new Order();

		createdOrder.setBranch(branch);
		createdOrder.setUser(user);
		createdOrder.setCustomer(customer);
		createdOrder.setOrderProducts(orderProducts);

		double totalPrice = calculateTotalPrice(orderProducts);
		createdOrder.setTotalPrice(totalPrice);

		createdOrder.setOrderStatus(Status.PENDING);
		createdOrder.setCreatedAt(LocalDateTime.now());

		try {
			orderRepository.insert(createdOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return createdOrder;
	}

	public boolean removeOrder(Order order) {
		try {
			orderRepository.delete(order);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean updateOrder(Order order) {
		Order updatedOrder = null;
		try {
			updatedOrder = orderRepository.save(order);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return updatedOrder != null;
	}

	public Order getOrderById(String oid) {
		return orderRepository.findById(oid).orElse(null);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	private double calculateTotalPrice(List<OrderProduct> orderProducts) {
		return orderProducts.stream().mapToDouble(OrderProduct::getRetailPrice)
				.filter(price -> !Double.isNaN(price) && !Double.isInfinite(price)).sum();
	}

	public Order updateOrderStatus(String orderId, Status newStatus) {
		Order existingOrder = orderRepository.findById(orderId).orElse(null);

		if (existingOrder != null) {
			existingOrder.setOrderStatus(newStatus);
			return orderRepository.save(existingOrder);
		}

		return null;
	}

}
