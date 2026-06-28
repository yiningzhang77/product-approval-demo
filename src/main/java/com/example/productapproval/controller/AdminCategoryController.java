package com.example.productapproval.controller;

import com.example.productapproval.dto.CategoryForm;
import com.example.productapproval.entity.ProductCategory;
import com.example.productapproval.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminCategoryController {

    private final ProductCategoryService productCategoryService;

    public AdminCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/admin/categories")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Boolean enabled,
                       Model model) {
        addListModel(keyword, enabled, model);
        model.addAttribute("categoryForm", new CategoryForm());
        return "admin/categories";
    }

    @PostMapping("/admin/categories")
    public String create(@Valid @ModelAttribute CategoryForm categoryForm,
                         BindingResult bindingResult,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Boolean enabled,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addListModel(keyword, enabled, model);
            return "admin/categories";
        }
        productCategoryService.create(categoryForm);
        redirectAttributes.addFlashAttribute("successMessage", "分类新增成功。");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) Integer sortOrder,
                         RedirectAttributes redirectAttributes) {
        CategoryForm form = new CategoryForm();
        form.setName(name);
        form.setSortOrder(sortOrder);
        productCategoryService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "分类修改成功。");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/enable")
    public String enable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productCategoryService.enable(id);
        redirectAttributes.addFlashAttribute("successMessage", "分类已启用。");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/disable")
    public String disable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productCategoryService.disable(id);
        redirectAttributes.addFlashAttribute("successMessage", "分类已停用。");
        return "redirect:/admin/categories";
    }

    private void addListModel(String keyword, Boolean enabled, Model model) {
        model.addAttribute("categories", productCategoryService.search(keyword, enabled));
        model.addAttribute("keyword", keyword);
        model.addAttribute("enabled", enabled);
        model.addAttribute("activeMenu", "admin-categories");
    }
}
