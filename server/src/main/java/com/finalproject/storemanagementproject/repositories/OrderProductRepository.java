package com.finalproject.storemanagementproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends MongoRepository<OrderProductRepository, Integer> {
}
