package com.example.productapproval.config;

import com.example.productapproval.service.rule.PriceWarningRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class RuleConfiguration {
    @Bean
    public PriceWarningRule priceWarningRule(
            @Value("${app.warning.price-threshold:1000}")
            BigDecimal threshold) {
        return new PriceWarningRule(threshold);
    }
}
