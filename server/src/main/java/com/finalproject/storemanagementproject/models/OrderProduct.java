package com.finalproject.storemanagementproject.models;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

//@Document(collection = "order_products")
@Data
public class OrderProduct {
//	@Id
	private String id;
	private Product product;
	private int quantity;
	private double importPrice;
	private double retailPrice;
}
