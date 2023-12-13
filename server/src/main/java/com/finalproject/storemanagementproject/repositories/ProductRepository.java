package com.finalproject.storemanagementproject.repositories;

import com.finalproject.storemanagementproject.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	  Optional<List<Product>> findByBarcode(String barcode);
    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Product>> searchByName(String name);
}
