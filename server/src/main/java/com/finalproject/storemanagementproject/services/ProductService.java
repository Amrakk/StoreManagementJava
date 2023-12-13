package com.finalproject.storemanagementproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.repositories.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProduct(){
		List<Product> list = new ArrayList<>();
		productRepository.findAll().forEach(list::add);
		return list;
	}

	public Product saveProduct(Product product) { return productRepository.save(product);
	}

	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}

	public Optional<Product> getProductById(String id) {
		return productRepository.findById(id);
	}
	public Product findByBarCode(String barcode) {
		return productRepository.findByBarcode(barcode).orElse(null);
	}
	
	public List<Product> findProductByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}
}
