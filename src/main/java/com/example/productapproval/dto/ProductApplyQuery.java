package com.example.productapproval.dto;

import com.example.productapproval.entity.ApprovalStatus;

public class ProductApplyQuery {

    private Long merchantId;
    private Long categoryId;
    private String keyword;
    private ApprovalStatus status;
    private Boolean warning;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }
}
