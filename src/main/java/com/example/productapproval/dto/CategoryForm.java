package com.example.productapproval.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryForm {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称不能超过100个字符")
    private String name;

    @Min(value = 0, message = "排序不能小于0")
    private Integer sortOrder = 100;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
