package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("inventories")
@Data
public class Inventory {
	@Id
	private Integer id;
	private String branchId;
	private String productId;
	private int availableQuantity;
}
