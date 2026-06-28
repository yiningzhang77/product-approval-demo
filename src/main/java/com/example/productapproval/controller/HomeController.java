package com.example.productapproval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/apply")
    public String oldApplyEntry() {
        return "redirect:/merchant/apply";
    }
}
