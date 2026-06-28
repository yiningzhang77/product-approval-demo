package com.example.productapproval.controller;

import com.example.productapproval.dto.ProductApplyForm;
import com.example.productapproval.dto.ProductApplyQuery;
import com.example.productapproval.entity.Merchant;
import com.example.productapproval.service.CsvExportService;
import com.example.productapproval.service.MerchantService;
import com.example.productapproval.service.ProductApplyService;
import com.example.productapproval.service.ProductCategoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class MerchantApplicationController {

    private final ProductApplyService productApplyService;
    private final MerchantService merchantService;
    private final ProductCategoryService productCategoryService;
    private final CsvExportService csvExportService;

    public MerchantApplicationController(ProductApplyService productApplyService,
                                         MerchantService merchantService,
                                         ProductCategoryService productCategoryService,
                                         CsvExportService csvExportService) {
        this.productApplyService = productApplyService;
        this.merchantService = merchantService;
        this.productCategoryService = productCategoryService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/merchant/apply")
    public String applyPage(@RequestParam(required = false) Long merchantId, Model model) {
        Merchant merchant = requireMerchant(merchantId, model);
        if (merchant == null) {
            return "error/merchant-required";
        }
        model.addAttribute("merchant", merchant);
        model.addAttribute("categories", productCategoryService.findEnabled());
        model.addAttribute("productApplyForm", new ProductApplyForm());
        model.addAttribute("activeMenu", "merchant-apply");
        return "merchant/apply";
    }

    @PostMapping("/merchant/apply")
    public String submit(@RequestParam(required = false) Long merchantId,
                         @Valid @ModelAttribute ProductApplyForm productApplyForm,
                         BindingResult bindingResult,
                         Model model) {
        Merchant merchant = requireMerchant(merchantId, model);
        if (merchant == null) {
            return "error/merchant-required";
        }
        model.addAttribute("merchant", merchant);
        model.addAttribute("categories", productCategoryService.findEnabled());
        model.addAttribute("activeMenu", "merchant-apply");
        if (bindingResult.hasErrors()) {
            return "merchant/apply";
        }

        try {
            productApplyService.create(merchantId, productApplyForm);
            model.addAttribute("productApplyForm", new ProductApplyForm());
            model.addAttribute("successMessage", "商品申请提交成功，请等待后台审批。");
            return "merchant/apply";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "merchant/apply";
        }
    }

    @GetMapping("/merchant/applications")
    public String applications(@RequestParam(required = false) Long merchantId,
                               @ModelAttribute ProductApplyQuery query,
                               Model model) {
        Merchant merchant = requireMerchant(merchantId, model);
        if (merchant == null) {
            return "error/merchant-required";
        }
        query.setMerchantId(merchantId);
        model.addAttribute("merchant", merchant);
        model.addAttribute("query", query);
        model.addAttribute("categories", productCategoryService.findEnabled());
        model.addAttribute("applications", productApplyService.search(query));
        model.addAttribute("activeMenu", "merchant-applications");
        return "merchant/applications";
    }

    @GetMapping("/merchant/applications/export")
    public void export(@RequestParam Long merchantId,
                       @ModelAttribute ProductApplyQuery query,
                       HttpServletResponse response) throws IOException {
        merchantService.requireEnabled(merchantId);
        query.setMerchantId(merchantId);
        csvExportService.exportProductApplications(response, "我的商品申请.csv", productApplyService.search(query));
    }

    private Merchant requireMerchant(Long merchantId, Model model) {
        try {
            return merchantService.requireEnabled(merchantId);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return null;
        }
    }
}
