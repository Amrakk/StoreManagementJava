package com.finalproject.storemanagementproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.Customer;
import com.finalproject.storemanagementproject.repositories.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer findByPhone(String phone) {
		return customerRepository.findByPhone(phone).orElse(null);
	}

//	public Customer findByEmail(String email) {
//		return customerRepository.findByEmail(email).orElse(null);
//	}
}
