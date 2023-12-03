package com.StoreManagementClient.Services;

import com.StoreManagementClient.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(@Value("${api.base.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl + "/users";
        this.restTemplate = restTemplate;
    }

    public User getUserById(Long id) {
        return restTemplate.getForObject(baseUrl + "/" + id, User.class);
    }

    public User createUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("username", user.getUsername());

        HttpEntity<?> requestEntity = new HttpEntity<>(payload, headers);
        System.out.println(requestEntity);
        return restTemplate.postForObject(baseUrl + "/users", requestEntity, User.class);
    }

}
