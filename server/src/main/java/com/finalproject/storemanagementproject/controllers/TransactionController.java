package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.dtos.OrderRequest;
import com.finalproject.storemanagementproject.models.*;
import com.finalproject.storemanagementproject.services.OrderService;
import com.finalproject.storemanagementproject.services.PaymentService;
import com.finalproject.storemanagementproject.services.ProductService;
import com.finalproject.storemanagementproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    // Create order with Status PENDING
    @PostMapping("/create")
    public ResponseEntity<APIResponse<Order>> createOrder(@RequestBody OrderRequest orderRequest) {
        String branchId = orderRequest.getBranchId();
        String uid = orderRequest.getUid();

        User user = userService.getUserById(uid);

        Order order = orderService.createOrder(user);

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.CREATED.value(), "Order has been created successfully",
                Collections.singletonList(order)));
    }

    // Should be in Product Controller
    @GetMapping("/product/{id}")
    public ResponseEntity<APIResponse<Product>> getProductById(@PathVariable String id) {
        Product product = productService.findByPid(id);
        Integer HTTP_CODE = HttpStatus.OK.value();
        String message = "Success";

        if (product == null) {
            HTTP_CODE = HttpStatus.NOT_FOUND.value();
            message = "Not found product";
        }

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(product)));
    }

    @GetMapping("/search-name")
    public ResponseEntity<APIResponse<Product>> searchProductsByName(@RequestParam String productName) {
        List<Product> searchResults = productService.findProductByName(productName);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Success", searchResults));
    }

    @GetMapping("/search-barcode")
    public ResponseEntity<APIResponse<Product>> searchProductByBarcode(@RequestParam String barcode) {
        Product products = productService.findByBarCode(barcode);

        Integer HTTP_CODE = HttpStatus.OK.value();
        String message = "Success";

        if (products == null) {
            HTTP_CODE = HttpStatus.NOT_FOUND.value();
            message = "Not found product";
        }

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(products)));
    }
    // End Product Section

    @GetMapping("/orders/{oid}")
    public ResponseEntity<APIResponse<Order>> getPaymentByOid(@PathVariable String oid) {
        Order order = orderService.getOrderById(oid);

        Integer HTTP_CODE = HttpStatus.OK.value();
        String message = "Success";

        if (order == null) {
            HTTP_CODE = HttpStatus.BAD_REQUEST.value();
            message = "Not found";
        }

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(order)));
    }

    @PostMapping("/orders/{oid}")
    public ResponseEntity<APIResponse<Order>> updateOrder(@PathVariable String oid, @RequestBody Order order) {
        Order existingOrder = orderService.getOrderById(oid);

        if (existingOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Invalid or non-pending order", null));
        }

        existingOrder.setOrderProducts(order.getOrderProducts());
        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setTotalPrice(order.getTotalPrice());

        boolean isUpdated = orderService.updateOrder(existingOrder);

        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new APIResponse<>(HttpStatus.NOT_MODIFIED.value(), "Failed to update", null));
        }

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Updated", Collections.singletonList(existingOrder)));
    }

    @PostMapping("/orders/{oid}/{payment}")
    public ResponseEntity<APIResponse<Payment>> processPayment(@PathVariable String oid, @PathVariable String payment) {
        Order executingOrder = orderService.getOrderById(oid);

        if (executingOrder == null || !Status.PENDING.equals(executingOrder.getOrderStatus())) {
            return ResponseEntity.badRequest().body(new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid or non-pending order", null));
        }

        boolean isPaid = paymentService.createPayment(executingOrder, payment);

        Integer HTTP_CODE = HttpStatus.OK.value();
        String url = "/orders/" + oid + "/payment/" + (isPaid ? "success" : "fail");

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, url, null));
    }

    @PostMapping("/orders/{oid}/update-status")
    public ResponseEntity<APIResponse<Order>> updateOrderStatus(
            @PathVariable String oid,
            @RequestParam Status newStatus) {
        Order updatedOrder = orderService.updateOrderStatus(oid, newStatus);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Order status updated successfully", Collections.singletonList(updatedOrder)));
    }

    @GetMapping("/orders/{oid}/payment/success")
    public ResponseEntity<APIResponse<Payment>> handlePaymentSuccess(@PathVariable String oid) {
        orderService.updateOrderStatus(oid, Status.COMPLETED);
        paymentService.updatePaymentStatus(oid, Status.COMPLETED);

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Payment successfully", null));
    }

    @GetMapping("/orders/{oid}/payment/fail")
    public ResponseEntity<APIResponse<Payment>> handlePaymentFailure(@PathVariable String oid) {
        orderService.updateOrderStatus(oid, Status.FAILED);
        paymentService.updatePaymentStatus(oid, Status.FAILED);

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Payment failed", null));
    }
}
