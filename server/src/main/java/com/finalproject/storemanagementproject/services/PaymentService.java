package com.finalproject.storemanagementproject.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.Payment;
import com.finalproject.storemanagementproject.models.Status;
import com.finalproject.storemanagementproject.repositories.PaymentRepository;

public class PaymentService {
	@Autowired
	private PaymentRepository paymentRepository;

	public Payment getPaymentById(String paymentid) {
		return paymentRepository.findById(paymentid).orElse(null);
	}

	public boolean createPayment(Order order, String paymentMethod) {
		Payment payment = new Payment();

		payment.setStatus(Status.PENDING);
		payment.setOid(order.getOid());
		payment.setPaymentTime(LocalDateTime.now());
		payment.setAmount(order.getTotalPrice());
		payment.setUid(order.getUser().getId());

		try {
			paymentRepository.insert(payment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Payment getPaymentByOid(String oid) {
		return paymentRepository.findByOid(oid).orElse(null);
	}

	public boolean updatePaymentMethod(String oid, String paymentMethod) {
		Payment existingPayment = getPaymentByOid(oid);

		if (existingPayment == null) {
			return false;
		}

		try {
			existingPayment.setPaymentMethod(paymentMethod);
			paymentRepository.save(existingPayment);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updatePaymentStatus(String oid, Status paymentStatus) {
		Payment existingPayment = getPaymentByOid(oid);

		if (existingPayment == null) {
			return false;
		}

		try {
			existingPayment.setStatus(paymentStatus);
			paymentRepository.save(existingPayment);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
