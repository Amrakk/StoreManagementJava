package com.finalproject.storemanagementproject.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

	Optional<Payment> findByOid(String oid);
}
