package com.example.productapproval.controller;

import com.example.productapproval.dto.ProductApplyQuery;
import com.example.productapproval.service.CsvExportService;
import com.example.productapproval.service.MerchantService;
import com.example.productapproval.service.ProductApplyService;
import com.example.productapproval.service.ProductCategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AdminApplicationController {

    private final ProductApplyService productApplyService;
    private final ProductCategoryService productCategoryService;
    private final MerchantService merchantService;
    private final CsvExportService csvExportService;

    public AdminApplicationController(ProductApplyService productApplyService,
                                      ProductCategoryService productCategoryService,
                                      MerchantService merchantService,
                                      CsvExportService csvExportService) {
        this.productApplyService = productApplyService;
        this.productCategoryService = productCategoryService;
        this.merchantService = merchantService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/admin/applications")
    public String list(@ModelAttribute ProductApplyQuery query, Model model) {
        addListModel(model, query, false);
        return "admin/applications";
    }

    @GetMapping("/admin/applications/warnings")
    public String warnings(@ModelAttribute ProductApplyQuery query, Model model) {
        query.setWarning(true);
        addListModel(model, query, true);
        return "admin/applications";
    }

    @PostMapping("/admin/applications/{id}/approve")
    public String approve(@PathVariable Long id,
                          @RequestParam(required = false) String approvalRemark,
                          RedirectAttributes redirectAttributes) {
        try {
            productApplyService.approve(id, approvalRemark);
            redirectAttributes.addFlashAttribute("successMessage", "审批通过成功。");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/applications";
    }

    @PostMapping("/admin/applications/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(required = false) String approvalRemark,
                         RedirectAttributes redirectAttributes) {
        try {
            productApplyService.reject(id, approvalRemark);
            redirectAttributes.addFlashAttribute("successMessage", "审批驳回成功。");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/applications";
    }

    @GetMapping("/admin/applications/export")
    public void export(@ModelAttribute ProductApplyQuery query, HttpServletResponse response) throws IOException {
        csvExportService.exportProductApplications(response, "商品申请筛选结果.csv", productApplyService.search(query));
    }

    private void addListModel(Model model, ProductApplyQuery query, boolean warningPage) {
        model.addAttribute("query", query);
        model.addAttribute("applications", productApplyService.search(query));
        model.addAttribute("categories", productCategoryService.findAllForSelect());
        model.addAttribute("merchants", merchantService.findAll());
        model.addAttribute("warningPage", warningPage);
        model.addAttribute("activeMenu", warningPage ? "admin-warnings" : "admin-applications");
    }
}
