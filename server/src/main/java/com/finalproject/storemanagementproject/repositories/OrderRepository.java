package com.finalproject.storemanagementproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
