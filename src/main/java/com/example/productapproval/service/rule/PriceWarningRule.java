package com.example.productapproval.service.rule;


import java.math.BigDecimal;

//boolean warning = productApply.getPrice() != null
//                && productApply.getPrice().compareTo(WARNING_PRICE) > 0;

public class PriceWarningRule {
    private final BigDecimal warningPrice;

    public PriceWarningRule(BigDecimal warningPrice) {
        this.warningPrice = warningPrice;
    }

    public boolean shouldWarn(BigDecimal price){
        if (price==null) {
            throw new IllegalArgumentException("价格不能为空");
        }
        return price.compareTo(warningPrice)>0;
    }

    public String warningReason(){
        return "超过预警价格"+warningPrice.toString();
    }
}
