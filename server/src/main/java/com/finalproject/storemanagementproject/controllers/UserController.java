package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.services.PasswordService;
import com.finalproject.storemanagementproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;

    @Autowired
    public UserController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();

        for (User user : users) {
            user.setPassword("");
            user.setAvatar("");
        }

        return ResponseEntity.ok(Map.of("message", "Get all users success", "users", users));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserById(String id) {
        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        user.setPassword("");

        return ResponseEntity.ok(Map.of("message", "Get user success", "user", user));
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public Map<String, Object> createUser(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()) != null)
            return Map.of("message", "Email already exists");

        return null;
    }

    @PostMapping(value = "/change-avatar/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> changeAvatar(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String avatarUrl = requestBody.get("avatarUrl");
        User user = userService.getUserById(id);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        if (!avatarUrl.matches("^https?://.*\\.(?:png|jpg|jpeg|gif)$"))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid avatar url"));

        user.setAvatar(avatarUrl);
        userService.updateUser(user);

        return ResponseEntity.ok(Map.of("message", "Change avatar success", "user", user));
    }

    @PostMapping(value = "/change-password/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("newPassword");
        String confirmPassword = requestBody.get("confirmPassword");

        User user = userService.getUserById(id);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        if (!newPassword.equals(confirmPassword))
            return ResponseEntity.badRequest().body(Map.of("message", "Confirm password not match"));

        newPassword = passwordService.hashPassword(newPassword);
        user.setPassword(newPassword);
        userService.updateUser(user);

        return ResponseEntity.ok().body(Map.of("message", "Change password success", "user", user));
    }

    @PostMapping(value = "/update/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @PathVariable User user) {
        return null;
    }

    @PostMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        return null;
    }
}
