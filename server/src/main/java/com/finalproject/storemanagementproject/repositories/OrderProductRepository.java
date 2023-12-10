package com.finalproject.storemanagementproject.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.OrderProduct;

@Repository
public interface OrderProductRepository extends MongoRepository<OrderProduct, Integer> {
	List<OrderProduct> findAllByOrderId(String oid);
}
