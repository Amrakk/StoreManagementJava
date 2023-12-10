package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("inventories")
@Data
public class Inventory {
	@Id
	private Integer inventoryId;
	private Branch branch;
	private Product product;
	private int availableQuantity;
}
