package com.example.productapproval.service.rule;

import java.math.BigDecimal;

public class PriceWarningRule {

    private final BigDecimal warningPrice;

    public PriceWarningRule(BigDecimal warningPrice) {
        if (warningPrice == null) {
            throw new IllegalArgumentException("预警价格不能为空");
        }
        this.warningPrice = warningPrice;
    }

    public boolean shouldWarn(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("价格不能为空");
        }
        return price.compareTo(warningPrice) > 0;
    }

    public String warningReason() {
        return "超过预警价格" + warningPrice.toPlainString();
    }

    public BigDecimal getThreshold() {
        return warningPrice;
    }
}
