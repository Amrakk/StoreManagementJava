package com.finalproject.storemanagementproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Inventory;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, Integer> {
}
