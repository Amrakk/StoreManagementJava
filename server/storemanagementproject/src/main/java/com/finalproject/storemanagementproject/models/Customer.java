package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "customers")
@Data
public class Customer {
	@Id
	private String id;
	private String name;
	private String phone;
	private String address;
}
