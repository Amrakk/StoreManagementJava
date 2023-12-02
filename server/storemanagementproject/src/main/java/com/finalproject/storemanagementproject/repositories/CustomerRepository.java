package com.finalproject.storemanagementproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
