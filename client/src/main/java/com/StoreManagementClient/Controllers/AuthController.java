package com.StoreManagementClient.Controllers;

import com.StoreManagementClient.Middlewares.Utils;
import com.StoreManagementClient.Models.User;
import com.StoreManagementClient.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Utils utils;

    @Autowired
    public AuthController(AuthService authService, Utils utils) {
        this.utils = utils;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        User user = utils.isAuthenticated(request);

        if (user != null) {
            redirectAttrs.addFlashAttribute("user", user);
            return "redirect:/Home";
        }

        return "Auth/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpServletResponse response,
                            Model model, RedirectAttributes redirectAttrs) {
        User user = authService.login(username, password, response);
        if (user == null) {
            model.addAttribute("error", "Invalid credentials!");
            return "Auth/login";
        }
        redirectAttrs.addFlashAttribute("user", user);
        return "redirect:/Home";
    }
}
