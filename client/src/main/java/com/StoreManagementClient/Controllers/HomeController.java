package com.StoreManagementClient.Controllers;

import com.StoreManagementClient.Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController implements ErrorController {

    @GetMapping("Home")
    public String index(Model model) {
        // for testing only
        model.addAttribute("user", new User(1L, "Guest"));
        return "index";
    }

    @RequestMapping("/error")
    public String handleException(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404)
                return "Error/404";
            else if (statusCode == 405)
                return "Error/405";
        }
        return "Error/500";
    }
}
