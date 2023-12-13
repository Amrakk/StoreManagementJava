package com.StoreManagementClient.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Branch {
	private String branchId;
	private String name;
	private String address;
}
