package com.finalproject.storemanagementproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
}
