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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findByBarCode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public List<Product> findProductByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product findByPid(String pid) {
        return productRepository.findById(pid).orElse(null);
    }
}
