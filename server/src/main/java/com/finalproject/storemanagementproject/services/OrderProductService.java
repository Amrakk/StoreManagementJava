package com.finalproject.storemanagementproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.OrderProduct;
import com.finalproject.storemanagementproject.repositories.OrderProductRepository;

@Service
public class OrderProductService {
	@Autowired
	private OrderProductRepository orderProductRepository;

	public boolean addOrderProduct(OrderProduct orderProduct) {
		OrderProduct addedProduct = null;
		try {
			addedProduct = orderProductRepository.insert(orderProduct);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return addedProduct != null;
	}

	public List<OrderProduct> getAllOrderProduct(String oid) {
		return orderProductRepository.findAllByOid(oid);
	}
}