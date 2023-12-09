package com.finalproject.storemanagementproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.storemanagementproject.services.OrderService;

public class TransactionController {
	@Autowired
	private OrderService orderService;
}
