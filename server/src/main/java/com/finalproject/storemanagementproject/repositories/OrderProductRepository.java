package com.finalproject.storemanagementproject.repositories;

import com.finalproject.storemanagementproject.models.OrderProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Repository
public interface OrderProductRepository extends MongoRepository<OrderProduct, Integer> {
	List<OrderProduct> findAllByOid(String oid);
}
