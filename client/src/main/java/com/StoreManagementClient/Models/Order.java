package com.StoreManagementClient.Models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Order {
	private String oid;
	private Branch branch;
	private Customer customer;
	private User user;
	private double totalPrice;
	private Status orderStatus;
	private List<OrderProduct> orderProducts;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
