package com.finalproject.storemanagementproject.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	Product findByQRCode(String QRCode);
    List<Product> findByNameContainingIgnoreCase(String name);
}
