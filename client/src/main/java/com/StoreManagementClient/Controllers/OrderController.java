package com.StoreManagementClient.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {	
    @GetMapping
    public String index() {
    	return "Order/invoice";
    }
}