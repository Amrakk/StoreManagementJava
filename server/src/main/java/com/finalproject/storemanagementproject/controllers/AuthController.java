package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.middleware.PasswordService;
import com.finalproject.storemanagementproject.middleware.TokenService;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordService passwordService;

    @Autowired
    public AuthController(UserService userService, TokenService tokenService, PasswordService passwordService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userService.getUserByUsername(username);
        if (user == null ||
                !passwordService.checkPassword(password, user.getPassword()) ||
                user.getStatus().toString().equals("LOCKED"))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials!"));

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .body(Map.of("message", "Login success", "user", user));
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String email = tokenService.validateToken(token);
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));

        return ResponseEntity.ok(Map.of("message", "Valid token", "user", user));
    }

}
