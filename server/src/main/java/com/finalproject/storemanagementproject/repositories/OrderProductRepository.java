package com.finalproject.storemanagementproject.repositories;

import com.finalproject.storemanagementproject.models.OrderProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderProductRepository extends MongoRepository<OrderProduct, String> {
    List<OrderProduct> findAllById(String oid);
}
