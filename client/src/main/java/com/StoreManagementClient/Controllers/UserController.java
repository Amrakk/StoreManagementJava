package com.StoreManagementClient.Controllers;

import com.StoreManagementClient.Models.User;
import com.StoreManagementClient.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String userList(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "Admin/user";
    }

    @PostMapping("/admin/users/create")
    public String createUser(@RequestParam("email") String email, @RequestParam("role") String role, RedirectAttributes redirectAttrs) {
        Object response = userService.createUser(email, role);

        if (response instanceof String)
            redirectAttrs.addFlashAttribute("error", response);
        else
            redirectAttrs.addFlashAttribute("success", "Create user success");

        List<User> users = userService.getAllUsers();
        redirectAttrs.addFlashAttribute("users", users);

        return "redirect:/admin/users";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);

        return "Main/profile";
    }

    @PostMapping("/user/change-avatar")
    public String changeAvatar(@RequestParam("avatarUrl") String avatarUrl, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        User user = (User) request.getAttribute("authenticatedUser");
        redirectAttrs.addFlashAttribute("user", user);

        Object response = userService.changeAvatar(user.getId(), avatarUrl);
        if (response instanceof String)
            redirectAttrs.addFlashAttribute("error", response);
        else {
            redirectAttrs.addFlashAttribute("success", "Change avatar success");
            redirectAttrs.addFlashAttribute("user", response);
        }

        return "redirect:/profile";
    }

    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpServletRequest request, RedirectAttributes redirectAttrs) {
        User user = (User) request.getAttribute("authenticatedUser");
        redirectAttrs.addFlashAttribute("user", user);

        Object response = userService.changePassword(user.getId(), newPassword, confirmPassword);
        if (response instanceof String)
            redirectAttrs.addFlashAttribute("error", response);
        else {
            redirectAttrs.addFlashAttribute("success", "Change password success");
            redirectAttrs.addFlashAttribute("user", response);
        }

        return "redirect:/profile";
    }

}
