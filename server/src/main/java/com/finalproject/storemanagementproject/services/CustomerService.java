package com.finalproject.storemanagementproject.services;

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

}
