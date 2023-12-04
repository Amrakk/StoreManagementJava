package com.StoreManagementClient.Services;

import com.StoreManagementClient.Middlewares.Converter;
import com.StoreManagementClient.Models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(@Value("${api.base.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl + "/auth";
        this.restTemplate = restTemplate;
    }

    public User login(String username, String password, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("username", username, "password", password);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, Object>> apiResponse = restTemplate.exchange(
                    baseUrl + "/login",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            String token = apiResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            token = token.substring(7);
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);

            User user = null;
            Map<String, Object> responseBody = apiResponse.getBody();
            if (responseBody != null && responseBody.containsKey("user"))
                user = Converter.convertToUser((Map<String, Object>) responseBody.get("user"));

            return user;
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
