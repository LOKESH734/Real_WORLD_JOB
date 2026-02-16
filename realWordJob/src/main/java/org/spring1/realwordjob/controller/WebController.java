package org.spring1.realwordjob.controller;

import org.spring1.realwordjob.model.User;
import org.spring1.realwordjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Signup page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // Handle signup form
    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        model.addAttribute("message", "Signup successful! Please login.");
        return "login";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Dashboard page
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}
