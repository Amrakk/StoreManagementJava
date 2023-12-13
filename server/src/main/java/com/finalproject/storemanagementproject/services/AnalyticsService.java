package com.finalproject.storemanagementproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.storemanagementproject.models.AnalyticsReport;
import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.Payment;
import com.finalproject.storemanagementproject.models.Status;

@Service
public class AnalyticsService {
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderService orderService;

	public AnalyticsReport getReportByTimeLine(String timeline) {
		LocalDateTime date;
		List<Payment> paymentsAtTime = new ArrayList<>();
		List<Order> orders;
		int totalOrders;
		int totalProducts;

		switch (timeline.toLowerCase()) {
		case "yesterday":
			date = LocalDateTime.now().minusDays(1);
			paymentsAtTime = paymentService.getPaymentByBetweenDate(date.toLocalDate(), LocalDate.now());
			orders = orderService.getOrdersByTimeAndStatus(date, LocalDateTime.now(), null);
			break;
		case "last7days":
			date = LocalDateTime.now().minusDays(7);
			paymentsAtTime = paymentService.getPaymentByBetweenDate(date.toLocalDate(), LocalDate.now());
			orders = orderService.getOrdersByTimeAndStatus(date, LocalDateTime.now(), null);
			break;
		case "thismonth":
			date = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
			paymentsAtTime = paymentService.getPaymentsInCurrentMonth(date.toLocalDate());
			orders = orderService.getOrdersByTimeAndStatus(date, LocalDateTime.now(), null);
			break;
		default:
			date = LocalDateTime.now().with(LocalTime.MIN);
			paymentsAtTime = paymentService.getPaymentByBetweenDate(date.toLocalDate(), LocalDate.now());
			orders = orderService.getOrdersByTimeAndStatus(date, LocalDateTime.now(), null);
			break;
		}

		totalOrders = (orders != null) ? orders.size() : 0;
		totalProducts = calculateTotalProducts(orders);

		AnalyticsReport report = new AnalyticsReport();

		double totalAmountReceived = calculateTotalAmount(paymentsAtTime, Status.COMPLETED);
		report.setTotalAmountReceived(totalAmountReceived);
		report.setNumberOfOrders(totalOrders);
		report.setNumberOfProducts(totalProducts);
		report.setOrders(orders);

		return report;
	}

	private int calculateTotalProducts(List<Order> orders) {
		return (orders != null) ? orders.stream().mapToInt(order -> order.getOrderProducts().size()).sum() : 0;
	}

	private double calculateTotalAmount(List<Payment> payments, Status targetStatus) {
		final Status finalTargetStatus = (targetStatus != null) ? targetStatus : Status.COMPLETED;

		return payments.stream().filter(p -> p.getStatus() == finalTargetStatus).mapToDouble(Payment::getAmount).sum();
	}

	public double calculateProfitForPeriod(String period) {
		LocalDate startDate;
		LocalDate endDate = LocalDate.now();

		switch (period.toLowerCase()) {
		case "yesterday":
			startDate = endDate.minusDays(1);
			break;
		case "7days":
			startDate = endDate.minusDays(7);
			break;
		case "thismonth":
			startDate = endDate.withDayOfMonth(1);
			break;
		default:
			String[] dateRange = period.split("-");
			startDate = LocalDate.parse(dateRange[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			endDate = LocalDate.parse(dateRange[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			break;
		}

		return calculateProfit(startDate, endDate);
	}

	public double calculateProfit(LocalDate startDate, LocalDate endDate) {
		List<Payment> completedPayments = paymentService.getPaymentByStatusAtDate(Status.COMPLETED, startDate, endDate);
		double totalRevenue = completedPayments.stream().mapToDouble(Payment::getAmount).sum();

		List<Order> orders = orderService.getOrdersByTimeAndStatus(startDate.atStartOfDay(),
				endDate.atTime(LocalTime.MAX), Status.COMPLETED);
		double totalCost = orders.stream().flatMap(order -> order.getOrderProducts().stream())
				.mapToDouble(orderProduct -> orderProduct.getImportPrice() * orderProduct.getQuantity()).sum();

		return totalRevenue - totalCost;
	}

}
