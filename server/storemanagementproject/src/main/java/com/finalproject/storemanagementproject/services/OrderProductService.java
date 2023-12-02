package com.finalproject.storemanagementproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.repositories.OrderProductRepository;

@Service
public class OrderProductService {
	@Autowired
	private OrderProductRepository orderProductRepository;
}
