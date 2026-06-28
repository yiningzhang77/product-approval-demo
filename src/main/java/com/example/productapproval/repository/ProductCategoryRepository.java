package com.example.productapproval.repository;

import com.example.productapproval.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findAllByEnabledOrderBySortOrderAscIdAsc(Boolean enabled);

    List<ProductCategory> findAllByOrderBySortOrderAscIdAsc();

    List<ProductCategory> findByNameContainingAndEnabledOrderBySortOrderAscIdAsc(String name, Boolean enabled);

    List<ProductCategory> findByNameContainingOrderBySortOrderAscIdAsc(String name);

    List<ProductCategory> findByEnabledOrderBySortOrderAscIdAsc(Boolean enabled);
}
