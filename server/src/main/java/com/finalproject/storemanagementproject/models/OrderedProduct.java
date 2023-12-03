package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "ordered_products")
@Data
public class OrderedProduct {
	@Id
	private Integer id;
	private String orderId;
	private String productId;
	private int quantity;
	private double importPrice;
	private double retailPrice;
}
