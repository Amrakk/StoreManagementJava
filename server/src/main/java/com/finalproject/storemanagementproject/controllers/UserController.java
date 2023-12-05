package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "", produces = "application/json")
    public Map<String, Object> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        int i = 0;
        for (User user : users) {
            user.setPassword("");
            user.setAvatar("");
        }

        return Map.of("message", "Get users success", "users", users);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Map<String, Object> getUserById(String id) {
        User user = userService.getUserById(id);
        if (user == null) return Map.of("message", "User not found");

        user.setPassword("");

        return Map.of("message", "Get user success", "user", user);
    }

    @PostMapping(value = "/change-avatar/{id}", produces = "application/json")
    public Map<String, Object> changeAvatar(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String avatarUrl = requestBody.get("avatarUrl");
        User user = userService.getUserById(id);
        if (user == null) return Map.of("message", "User not found");

        if (!avatarUrl.matches("^https?://.*\\.(?:png|jpg|jpeg|gif)$"))
            return Map.of("message", "Invalid image url");

        user.setAvatar(avatarUrl);
        userService.updateUser(user);

        return Map.of("message", "Change avatar success", "user", user);
    }

}
