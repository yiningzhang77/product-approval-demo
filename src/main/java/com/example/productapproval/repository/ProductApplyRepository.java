package com.example.productapproval.repository;

import com.example.productapproval.entity.ProductApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductApplyRepository extends JpaRepository<ProductApply, Long>, JpaSpecificationExecutor<ProductApply> {

    List<ProductApply> findAllByOrderByCreatedAtDesc();

    long countByMerchantId(Long merchantId);

    long countByMerchantIdAndStatus(Long merchantId, com.example.productapproval.entity.ApprovalStatus status);

    long countByMerchantIdAndWarning(Long merchantId, Boolean warning);

    List<ProductApply> findTop5ByMerchantIdOrderByCreatedAtDesc(Long merchantId);

    List<ProductApply> findTop5ByStatusOrderByCreatedAtDesc(com.example.productapproval.entity.ApprovalStatus status);

    List<ProductApply> findTop5ByWarningOrderByCreatedAtDesc(Boolean warning);
}
