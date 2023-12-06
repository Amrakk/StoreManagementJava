package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.middleware.PasswordService;
import com.finalproject.storemanagementproject.middleware.TokenService;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public static final String ADMIN_RESOURCE = "ADMIN";
    public static final String OWNER_RESOURCE = "OWNER";
    public static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
    public static final String INVALID_TOKEN_MESSAGE = "Invalid token";
    public static final String NO_PERMISSION_MESSAGE = "You don't have permission to access this resource";

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
    public ResponseEntity<Map<String, Object>> validate(@RequestHeader("Authorization") String token,
                                                        @RequestBody Map<String, String> body,
                                                        HttpServletRequest request) {
        String resource = body.get("resource");

        token = token.substring(7);
        String email = tokenService.validateToken(token);
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));

        if (resource.equalsIgnoreCase(ADMIN_RESOURCE)) {
            if (!user.getRole().toString().equalsIgnoreCase("ADMIN") && !user.getRole().toString().equalsIgnoreCase("OWNER")) {
                return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", NO_PERMISSION_MESSAGE));
            }
        } else if (resource.equalsIgnoreCase(OWNER_RESOURCE)) {
            if (!user.getRole().toString().equalsIgnoreCase("OWNER")) {
                return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", NO_PERMISSION_MESSAGE));
            }
        }

        return ResponseEntity.ok(Map.of("message", "Valid token", "user", user));
    }

}
