package com.pharmondev.springtwofactor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {
    @GetMapping("/login")
    String login() {
        return "login";
    }
    @PostMapping("/logout")
    public String performLogout() {
        return "redirect:/login";
    }
}
