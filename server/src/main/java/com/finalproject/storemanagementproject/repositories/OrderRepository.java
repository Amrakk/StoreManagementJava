package com.finalproject.storemanagementproject.repositories;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByOrderStatus(Status status);

    List<Order> findByCreatedAtBetweenAndOrderStatus(LocalDateTime startDateTime, LocalDateTime endDateTime, Status status);

    List<Order> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Order> findByCustomerCustId(String custId);
}
