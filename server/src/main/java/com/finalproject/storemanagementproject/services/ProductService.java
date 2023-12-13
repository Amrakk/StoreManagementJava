package com.finalproject.storemanagementproject.services;

import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findByBarCode(String barcode) {
        return productRepository.findByBarcode(barcode).orElse(null);
    }

    public List<Product> findProductByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
