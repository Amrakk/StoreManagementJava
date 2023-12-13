package com.finalproject.storemanagementproject.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.storemanagementproject.models.APIResponse;
import com.finalproject.storemanagementproject.models.Customer;
import com.finalproject.storemanagementproject.services.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@GetMapping
	public ResponseEntity<APIResponse<Customer>> getCustomerByPhone(@RequestParam String phone) {
		Customer customer = customerService.findByPhone(phone);

		if (customer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Not Found", Collections.singletonList(customer)));
		}

		return ResponseEntity
				.ok(new APIResponse<>(HttpStatus.OK.value(), "Success", Collections.singletonList(customer)));
	}

	@PostMapping("/create")
	public ResponseEntity<APIResponse<Customer>> createCustomer(@RequestBody Customer customer) {
		customer.setPoint(Double.valueOf(0));
		boolean isCreated = customerService.createCustomer(customer);
		
		if (!isCreated) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to add", Collections.singletonList(customer)));
		}
		
		return ResponseEntity
				.ok(new APIResponse<>(HttpStatus.CREATED.value(), "Success", Collections.singletonList(customer)));
	}
}
