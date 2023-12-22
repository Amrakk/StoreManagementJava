package com.finalproject.storemanagementproject.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

	public AnalyticsReport getReportByTimeLine(String timeline, Instant startDate, Instant endDate) {
		Instant date;
		List<Payment> paymentsAtTime = new ArrayList<>();
		List<Order> orders;
		int totalOrders;
		int totalProducts;
	
		ZoneId zoneId = ZoneId.of("UTC");
		LocalDate currentDate = LocalDate.now(zoneId);
		Instant now = currentDate.atStartOfDay().atZone(zoneId).toInstant();
		
		switch (timeline.toLowerCase()) {
		case "yesterday":
			System.out.println("IN YESTERDAY");
		    Instant yesterdayStart = now.minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
		    Instant yesterdayEnd = now.truncatedTo(ChronoUnit.DAYS);

		    paymentsAtTime = paymentService.getPaymentByBetweenDate(yesterdayStart, yesterdayEnd);
		    orders = orderService.getOrdersByTimeAndStatus(yesterdayStart, yesterdayEnd, null);
		    break;
		case "last7days":
		    System.out.println("IN LAST7DAYS");
		    Instant sevenDaysAgo = now.minus(7, ChronoUnit.DAYS);

		    paymentsAtTime = paymentService.getPaymentByBetweenDate(sevenDaysAgo, now);
		    orders = orderService.getOrdersByTimeAndStatus(sevenDaysAgo, now, null);
		    break;
		case "thismonth":
		    System.out.println("IN THIS MONTH");
		    Instant startOfMonth = now.atZone(ZoneOffset.UTC).withDayOfMonth(1).toInstant();

		    paymentsAtTime = paymentService.getPaymentsInCurrentMonth(startOfMonth.atZone(ZoneOffset.UTC).toLocalDate());
		    orders = orderService.getOrdersByTimeAndStatus(startOfMonth, now, null);
		    break;
		case "custom":
			System.out.println(startDate);
			System.out.println(endDate);
			if (startDate == null || endDate == null) {
				return null;
			}

			paymentsAtTime = paymentService.getPaymentByBetweenDate(startDate, endDate);
			orders = orderService.getOrdersByTimeAndStatus(startDate, endDate, null);
			break;
		default:
			System.out.println("IN TODAY");
			date = LocalDateTime.now().with(LocalTime.MIN).toInstant(ZoneOffset.UTC);
			paymentsAtTime = paymentService.getPaymentByBetweenDate(date, Instant.now());
			orders = orderService.getOrdersByTimeAndStatus(date, Instant.now(), null);
			orders.forEach(ord -> System.out.println(ord.toString()));
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
		return (orders != null)
				? orders.stream().filter(order -> order.getOrderProducts() != null)
						.mapToInt(order -> order.getOrderProducts().size()).sum()
				: 0;
	}

	private double calculateTotalAmount(List<Payment> payments, Status targetStatus) {
		final Status finalTargetStatus = (targetStatus != null) ? targetStatus : Status.COMPLETED;

		return payments.stream().filter(p -> p.getStatus() == finalTargetStatus).mapToDouble(Payment::getAmount).sum();
	}
	
	public double calculateProfitForPeriod(String period) {
	    Instant startDate;
	    Instant endDate = Instant.now();

	    switch (period.toLowerCase()) {
	        case "yesterday":
	            startDate = endDate.minus(1, ChronoUnit.DAYS);
	            break;
	        case "7days":
	            startDate = endDate.minus(7, ChronoUnit.DAYS);
	            break;
	        case "thismonth":
	            startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC);
	            break;
	        default:
	            String[] dateRange = period.split("-");
	            startDate = LocalDate.parse(dateRange[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
	                .atStartOfDay()
	                .toInstant(ZoneOffset.UTC);
	            endDate = LocalDate.parse(dateRange[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
	                .plusDays(1)
	                .atStartOfDay()
	                .toInstant(ZoneOffset.UTC);
	            break;
	    }

	    return calculateProfit(startDate, endDate);
	}

	public double calculateProfit(Instant startDate, Instant endDate) {
	    List<Payment> completedPayments = paymentService.getPaymentByStatusAtDate(Status.COMPLETED, startDate, endDate);
	    double totalRevenue = completedPayments.stream().mapToDouble(Payment::getAmount).sum();

	    List<Order> orders = orderService.getOrdersByTimeAndStatus(startDate, endDate, Status.COMPLETED);

	    double totalCost = orders.stream()
	            .flatMap(order -> order.getOrderProducts().stream())
	            .mapToDouble(orderProduct -> orderProduct.getImportPrice() * orderProduct.getQuantity())
	            .sum();

	    return totalRevenue - totalCost;
	}

}
