package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "branches")
@Data
public class Branch {
	@Id
	private String id;
	private String name;
	private String address;
}
