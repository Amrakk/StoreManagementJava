package com.finalproject.storemanagementproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.repositories.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public Product findByBarCode(String barcode) {
		return productRepository.findByBarCode(barcode).orElse(null);
	}
	
	public List<Product> findProductByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}
}
