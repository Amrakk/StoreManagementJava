package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.middleware.MailService;
import com.finalproject.storemanagementproject.middleware.PasswordService;
import com.finalproject.storemanagementproject.models.Role;
import com.finalproject.storemanagementproject.models.Status;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;
    private final MailService mailService;

    @Value("${default.avatar.url}")
    private String defaultAvatarUrl;

    @Autowired
    public UserController(UserService userService, PasswordService passwordService, MailService mailService) {
        this.mailService = mailService;
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @GetMapping(value = "/admin/users", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam(required = false) String text) {
        Iterable<User> users = null;
        if (text != null && !text.isEmpty()) users = userService.searchUser(text);
        else users = userService.getAllUsers();

        for (User user : users) user.setPassword("");

        return ResponseEntity.ok(Map.of("message", "Get users success", "users", users));
    }

    @GetMapping(value = "/admin/users/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        user.setPassword("");

        return ResponseEntity.ok(Map.of("message", "Get user success", "user", user));
    }

    @PostMapping(value = "/admin/users/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String role = body.get("role").toUpperCase();

        if (email.isEmpty() || role.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Please fill all fields"));

        if (!email.matches("^\\w+@(" + mailService.DOMAIN + ")$"))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or domain"));

        if (userService.getUserByEmail(email) != null)
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));

        if (!userService.isValidRole(role))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));

        String username = email.split("@")[0];
        String password = passwordService.hashPassword(username);

        User user = new User(null, email, username, password, Status.NORMAL, Role.valueOf(role), defaultAvatarUrl);

        boolean isAdded = userService.addUser(user);

        if (!isAdded)
            return ResponseEntity.badRequest().body(Map.of("message", "Create user failed"));

        return ResponseEntity.ok(Map.of("message", "Create user success", "user", user));
    }

    @PostMapping(value = "/admin/users/update/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @RequestBody Map<String, String> body) {
        String role = body.get("role").toUpperCase();
        String status = body.get("status").toUpperCase();

        if (role.isEmpty() || status.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Please fill all fields"));

        if (!userService.isValidRole(role))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));

        if (!userService.isValidStatus(status))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid status"));

        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        user.setRole(Role.valueOf(role));
        user.setStatus(Status.valueOf(status));

        boolean isUpdated = userService.updateUser(user);
        if (!isUpdated)
            return ResponseEntity.badRequest().body(Map.of("message", "Update user failed"));

        return ResponseEntity.ok(Map.of("message", "Update user success", "user", user));
    }

    @PostMapping(value = "/admin/users/delete/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        if (user.getRole().equals(Role.OWNER))
            return ResponseEntity.badRequest().body(Map.of("message", "Can not delete owner"));

        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted)
            return ResponseEntity.badRequest().body(Map.of("message", "Delete user failed"));

        return ResponseEntity.ok(Map.of("message", "Delete user success", "user", user));

    }

    @PostMapping(value = "/users/change-avatar/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> changeAvatar(@PathVariable String id, @RequestBody Map<String, String> body) {
        String avatarUrl = body.get("avatarUrl");
        User user = userService.getUserById(id);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        if (!avatarUrl.matches("^https?://.*\\.(?:png|jpg|jpeg|gif)$"))
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid avatar url"));

        user.setAvatar(avatarUrl);
        boolean isUpdated = userService.updateUser(user);
        if (!isUpdated) return ResponseEntity.badRequest().body(Map.of("message", "Change avatar failed"));

        return ResponseEntity.ok(Map.of("message", "Change avatar success", "user", user));
    }

    @PostMapping(value = "/users/change-password/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable String id, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");

        User user = userService.getUserById(id);
        if (user == null) return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        if (newPassword.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));

        if (!newPassword.equals(confirmPassword))
            return ResponseEntity.badRequest().body(Map.of("message", "Confirm password not match"));

        newPassword = passwordService.hashPassword(newPassword);
        user.setPassword(newPassword);
        boolean isUpdated = userService.updateUser(user);
        if (!isUpdated) return ResponseEntity.badRequest().body(Map.of("message", "Change password failed"));

        return ResponseEntity.ok().body(Map.of("message", "Change password success", "user", user));
    }

}
