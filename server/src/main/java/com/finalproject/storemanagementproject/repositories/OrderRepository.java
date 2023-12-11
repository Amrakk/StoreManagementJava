package com.finalproject.storemanagementproject.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.Status;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
	List<Order> findByStatus(Status status);
    List<Order> findByCreatedAtBetweenAndStatus(LocalDateTime startDateTime, LocalDateTime endDateTime, Status status);
    List<Order> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
