package com.example.productapproval.controller;

import com.example.productapproval.entity.Merchant;
import com.example.productapproval.service.MerchantService;
import com.example.productapproval.service.ProductApplyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MerchantDashboardController {

    private final MerchantService merchantService;
    private final ProductApplyService productApplyService;

    public MerchantDashboardController(MerchantService merchantService, ProductApplyService productApplyService) {
        this.merchantService = merchantService;
        this.productApplyService = productApplyService;
    }

    @GetMapping("/merchant/dashboard")
    public String dashboard(@RequestParam(required = false) Long merchantId, Model model) {
        try {
            Merchant merchant = merchantService.requireEnabled(merchantId);
            model.addAttribute("merchant", merchant);
            model.addAttribute("stats", productApplyService.getMerchantStats(merchantId));
            model.addAttribute("recentApplications", productApplyService.findRecentForMerchant(merchantId));
            model.addAttribute("activeMenu", "merchant-dashboard");
            return "merchant/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error/merchant-required";
        }
    }
}
