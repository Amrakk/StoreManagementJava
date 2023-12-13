package com.StoreManagementClient.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {	
    @GetMapping
    public String index() {
//        String url = "http://localhost:8080/api/transactions/create";
//
//        OrderRequest orderRequest = new OrderRequest();
//        orderRequest.setBranchId("B001");
//        orderRequest.setUid("656c99b0bd41d878152987ad");
//
//        WebClient webClient = WebClient.create();
//        try {
//            String responseBody = webClient.post()
//                    .uri(url)
//                    .body(Mono.just(orderRequest), OrderRequest.class)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//            
//            JsonNode jsonNode = objectMapper.readTree(responseBody);
//            
//            String oid = jsonNode.path("data").get(0).path("oid").asText();
//
//            model.addAttribute("oid", oid);
//            return "Order/invoice";
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        return "Error/500";
    	return "Order/invoice";
    }
}