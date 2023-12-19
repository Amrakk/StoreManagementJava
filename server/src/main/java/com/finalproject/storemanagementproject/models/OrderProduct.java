package com.finalproject.storemanagementproject.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Document(collection = "order_products")
@Data
public class OrderProduct {
	@Id
	private String id;
	@Field("products")
	private String pid;
	@Field("orders")
	private String oid;
	private int quantity;
	private double importPrice;
	private double retailPrice;
}
