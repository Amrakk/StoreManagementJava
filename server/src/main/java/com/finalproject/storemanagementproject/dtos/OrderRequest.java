package com.finalproject.storemanagementproject.dtos;

import lombok.Data;

@Data
public class OrderRequest {
	private String oid;
	private String branchId;
	private String uid;
}
