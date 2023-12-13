package com.StoreManagementClient.Models;

import lombok.Data;

@Data
public class Customer {
	private String custId;
	private String name;
	private String phone;
	private String email;
	private Double point;
}
