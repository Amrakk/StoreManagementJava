package com.finalproject.storemanagementproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.repositories.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	public boolean addOrder(Order order) {
		Order addedOrder = null;
		try {
			addedOrder = orderRepository.insert(order);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return addedOrder != null;
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
}
