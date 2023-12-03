package com.StoreManagementClient.Middlewares;

import com.StoreManagementClient.Models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class Utils {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public Utils(@Value("${api.base.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public User isAuthenticated(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        String token = null;
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName() + " " + cookie.getValue());
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) return null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    baseUrl + "/auth/validate",
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> responseBody = response.getBody();

            User user = null;

            if (responseBody != null && responseBody.containsKey("user"))
                user = Converter.convertToUser((Map<String, Object>) responseBody.get("user"));

            return user;
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

}
