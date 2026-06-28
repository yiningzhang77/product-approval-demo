package com.example.productapproval.service;

import com.example.productapproval.dto.CategoryForm;
import com.example.productapproval.entity.ProductCategory;
import com.example.productapproval.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductCategory> findEnabled() {
        return productCategoryRepository.findAllByEnabledOrderBySortOrderAscIdAsc(true);
    }

    @Transactional(readOnly = true)
    public List<ProductCategory> findAllForSelect() {
        return productCategoryRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    @Transactional(readOnly = true)
    public List<ProductCategory> search(String keyword, Boolean enabled) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword && enabled != null) {
            return productCategoryRepository.findByNameContainingAndEnabledOrderBySortOrderAscIdAsc(keyword.trim(), enabled);
        }
        if (hasKeyword) {
            return productCategoryRepository.findByNameContainingOrderBySortOrderAscIdAsc(keyword.trim());
        }
        if (enabled != null) {
            return productCategoryRepository.findByEnabledOrderBySortOrderAscIdAsc(enabled);
        }
        return productCategoryRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    @Transactional(readOnly = true)
    public ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品分类不存在"));
    }

    @Transactional
    public void create(CategoryForm form) {
        LocalDateTime now = LocalDateTime.now();
        ProductCategory category = new ProductCategory();
        category.setName(form.getName());
        category.setSortOrder(defaultSortOrder(form.getSortOrder()));
        category.setEnabled(true);
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        productCategoryRepository.save(category);
    }

    @Transactional
    public void update(Long id, CategoryForm form) {
        ProductCategory category = findById(id);
        category.setName(form.getName());
        category.setSortOrder(defaultSortOrder(form.getSortOrder()));
        category.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void enable(Long id) {
        ProductCategory category = findById(id);
        category.setEnabled(true);
        category.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void disable(Long id) {
        ProductCategory category = findById(id);
        category.setEnabled(false);
        category.setUpdatedAt(LocalDateTime.now());
    }

    private int defaultSortOrder(Integer sortOrder) {
        return sortOrder == null ? 100 : sortOrder;
    }
}
