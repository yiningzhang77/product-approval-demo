package com.example.productapproval.controller;

import com.example.productapproval.service.ProductApplyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final ProductApplyService productApplyService;

    public AdminDashboardController(ProductApplyService productApplyService) {
        this.productApplyService = productApplyService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", productApplyService.getAdminStats());
        model.addAttribute("recentPending", productApplyService.findRecentPending());
        model.addAttribute("recentWarnings", productApplyService.findRecentWarnings());
        model.addAttribute("activeMenu", "admin-dashboard");
        return "admin/dashboard";
    }
}
