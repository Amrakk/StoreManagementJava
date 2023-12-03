package com.finalproject.storemanagementproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.storemanagementproject.models.Branch;

@Repository
public interface BranchRepository extends MongoRepository<Branch, String> {

}
