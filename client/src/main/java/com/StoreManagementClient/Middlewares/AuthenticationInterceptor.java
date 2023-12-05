package com.StoreManagementClient.Middlewares;

import com.StoreManagementClient.Models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Service
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationInterceptor(@Value("${api.base.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/error")) return true;

        User user = isAuthenticated(request, response);
        if (user == null) {
            if (requestURI.equals("/auth/login")) return true;

            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/auth/login");
            return false;
        }
        
        request.setAttribute("authenticatedUser", user);
        if (requestURI.equals("/") || requestURI.equals("/home")) response.sendRedirect("/Home");
        return true;
    }

    public User isAuthenticated(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null || token.isEmpty()) return null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        try {
            ResponseEntity<Map<String, Object>> apiResponse = restTemplate.exchange(
                    baseUrl + "/auth/validate",
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> responseBody = apiResponse.getBody();
            User user = null;

//            System.out.println(responseBody);
//            System.out.println(responseBody.containsKey("user"));

            if (responseBody == null || !responseBody.containsKey("user")) {
                Cookie cookie = new Cookie("token", "");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            } else
                user = Converter.convertToUser((Map<String, Object>) responseBody.get("user"));

            return user;
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}