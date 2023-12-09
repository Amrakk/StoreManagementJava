package com.finalproject.storemanagementproject.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "orders")
@Data
public class Order {
	@Id
	private String oid;
	private Branch branch;
	private Customer customer;
	private User user;
	private double totalPrice;
	private Status orderStatus;
	private List<Product> products;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
