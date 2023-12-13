package com.finalproject.storemanagementproject.services;

import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.repositories.ProductRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> findByBarCode(String barcode) {
		return productRepository.findByBarcode(barcode).orElse(new ArrayList<>());
	}
	
	public List<Product> findProductByName(String name) {
		return productRepository.searchByName(".*" + name + ".*").orElse(new ArrayList<>());
	}
	
	public Product findByPid(String pid) {
		return productRepository.findById(pid).orElse(null);
	}
}
