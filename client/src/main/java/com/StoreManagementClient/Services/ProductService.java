package com.StoreManagementClient.Services;

import com.StoreManagementClient.Middlewares.Converter;
import com.StoreManagementClient.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(@Value("${api.base.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl + "/product";
        this.restTemplate = restTemplate;
    }

    public List<Product> getProducts(String text) {
        String url = baseUrl;
        if (text != null && !text.isEmpty()) url += "?text=" + text;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map<String, Object>> apiResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            List<Product> products = null;
            Map<String, Object> responseBody = apiResponse.getBody();
            if (responseBody != null && responseBody.containsKey("data"))
                products = Converter.convertToProducts((List<Map<String, Object>>) responseBody.get("data"));

            return products;
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
