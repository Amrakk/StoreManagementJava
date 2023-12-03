package com.StoreManagementClient.Controllers;

import com.StoreManagementClient.Models.User;
import com.StoreManagementClient.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpRequest request, Model model) {
        List<String> authorizes = request.getHeaders().get("Authorization");
        if (authorizes != null) {
            String token = authorizes.get(0).split(" ")[1];
            User user = userService.validateToken(token);
            if (user != null) {
                model.addAttribute("user", user);
                return "redirect:/Home";
            }
        }

        return "Auth/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        System.out.println(username + " " + password);
        if (!username.equals("admin")) {
            model.addAttribute("error", "Invalid username or password");
            return "Auth/login";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register() {
        return "Auth/register";
    }

}
