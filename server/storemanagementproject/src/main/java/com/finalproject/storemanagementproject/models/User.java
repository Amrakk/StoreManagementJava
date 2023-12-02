package com.finalproject.storemanagementproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {
	@Id
	private String id;
	private String email;
	private String username;
	private String password;
	private Status status;
	private Role role;
	private String avatar;
}
