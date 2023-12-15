package com.finalproject.storemanagementproject.services;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.OrderProduct;
import com.finalproject.storemanagementproject.models.Status;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(User user) {
        Order createdOrder = new Order();

        createdOrder.setUser(user);

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

    @Transactional
    public boolean updateOrder(Order order) {
        try {
            Order updatedOrder = orderRepository.save(order);
            return updatedOrder != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
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

    public List<Order> getOrdersByStatus(Status status) {
        if (status != null) {
            return orderRepository.findByOrderStatus(status);
        } else {
            return orderRepository.findAll();
        }
    }

    public List<Order> getOrdersByTimeAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status) {
        if (status != null) {
            return orderRepository.findByCreatedAtBetweenAndOrderStatus(startDate, endDate, status);
        } else {
            return orderRepository.findByCreatedAtBetween(startDate, endDate);
        }
    }

    public List<Order> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerCustId(customerId);
    }
}
