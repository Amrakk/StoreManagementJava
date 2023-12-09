package com.finalproject.storemanagementproject.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "products")
@Data
public class Product {
	@Id
	private String id;
	private String name;
	private Category category;
	private double importPrice;
	private double retailPrice;
	private String barcode;
	private String illustrator;
	private int importedQuantity;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
