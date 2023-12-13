package com.finalproject.storemanagementproject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	Optional<Product> findByBarcode(String barcode);
    List<Product> findByNameContainingIgnoreCase(String name);
}
