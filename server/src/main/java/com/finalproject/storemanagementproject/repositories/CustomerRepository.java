package com.finalproject.storemanagementproject.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
	Optional<Customer> findByPhone(String phone);
//	Optional<Customer> findByEmail(String email);
}
