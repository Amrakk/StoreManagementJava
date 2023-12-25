package com.finalproject.storemanagementproject.services;

import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.OrderProduct;
import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.repositories.OrderRepository;
import com.finalproject.storemanagementproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}

	public Product getProductById(String id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<Product> findByBarCode(String barcode) {
		return productRepository.findByBarcode(barcode);
	}

	public List<Product> findProductByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}

	public Product findByPid(String pid) {
		return productRepository.findById(pid).orElse(null);
	}

	public List<Product> getTop5ProductByTime(String timeline, Instant startDate, Instant endDate) {
		Instant start, end;
		Instant now = Instant.now(Clock.offset(Clock.systemUTC(), Duration.ofHours(+7)));

		switch (timeline.toLowerCase()) {
		case "yesterday":
			System.out.println("IN YESTERDAY");
			start = now.minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
			end = now.truncatedTo(ChronoUnit.DAYS);

			break;
		case "last7days":
			System.out.println("IN LAST7DAYS");
			start = now.minus(7, ChronoUnit.DAYS);
			end = now.truncatedTo(ChronoUnit.DAYS);
			break;
		case "thismonth":
			System.out.println("IN THIS MONTH");
			start = now.atZone(ZoneOffset.UTC).withDayOfMonth(1).toInstant();
			end = now.truncatedTo(ChronoUnit.DAYS);

			break;
		case "custom":
			System.out.println(startDate);
			System.out.println(endDate);
			if (startDate == null || endDate == null) {
				return null;
			}

			start = startDate;
			end = endDate;
			break;

		default:
			System.out.println("IN TODAY");
			start = now.truncatedTo(ChronoUnit.DAYS);
			end = now.plus(1, ChronoUnit.DAYS);

			break;
		}

		List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
		
		Map<String, Integer> productQuantityMap = new HashMap<>();

		for (Order order : orders) {
			for (OrderProduct orderProduct : order.getOrderProducts()) {
				String productId = orderProduct.getPid();
				int quantity = orderProduct.getQuantity();

				productQuantityMap.put(productId, productQuantityMap.getOrDefault(productId, 0) + quantity);
			}
		}

		List<Product> topProducts = productQuantityMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(5)
				.map(entry -> productRepository.findById(entry.getKey()).orElse(null)).filter(Objects::nonNull)
				.collect(Collectors.toList());

		return topProducts;
	}
}
