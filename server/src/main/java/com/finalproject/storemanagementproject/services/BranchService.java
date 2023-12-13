package com.finalproject.storemanagementproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Branch;
import com.finalproject.storemanagementproject.repositories.BranchRepository;

@Service
public class BranchService {
	@Autowired
	private BranchRepository branchRepository;
	
	public Branch getBranchById(String branchId) {
		return branchRepository.findById(branchId).orElse(null);
	}
}
