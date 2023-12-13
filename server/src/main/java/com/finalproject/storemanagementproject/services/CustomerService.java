package com.finalproject.storemanagementproject.services;

import org.springframework.transaction.annotation.Transactional;
import com.finalproject.storemanagementproject.models.Customer;
import com.finalproject.storemanagementproject.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer findByPhone(String phone) {
		return customerRepository.findByPhone(phone).orElse(null);
	}
	
	public Customer findByEmail(String email) {
		return customerRepository.findByEmail(email).orElse(null);
	}
	
	@Transactional
	public boolean createCustomer(Customer customer) {
		try {
			customerRepository.save(customer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
