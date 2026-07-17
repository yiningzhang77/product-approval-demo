package com.example.productapproval.service.rule;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceWarningRuleTest {
    @Test
    void shouldNotWarnWhenPriceEqualsThreshold(){
        //Given
        PriceWarningRule rule= new PriceWarningRule(new BigDecimal("1000"));

        //When
        boolean result = rule.shouldWarn(new BigDecimal("1000"));

        //Then
        assertFalse(result);
    }
    @Test
    void shouldWarnWhenPriceBiggerThanThreshold(){
        PriceWarningRule rule= new PriceWarningRule(new BigDecimal("1000"));
        boolean result = rule.shouldWarn(new BigDecimal("1000.01"));
        assertTrue(result);
    }
    @Test
    void shouldThrowExceptionWhenPriceIsNull(){
        PriceWarningRule rule= new PriceWarningRule(new BigDecimal("1000"));
        assertThrows(IllegalArgumentException.class, () -> rule.shouldWarn(null));
    }
    @Test
    void shouldNotWarnWhenPriceSmallerThanThreshold(){
        //Given
        PriceWarningRule rule= new PriceWarningRule(new BigDecimal("1000"));

        //When
        boolean result = rule.shouldWarn(new BigDecimal("999.99"));

        //Then
        assertFalse(result);
    }
    @Test
    void shouldProduceDifferentResultsForSamePriceUnderDifferentThresholds(){
        PriceWarningRule lowerThresholdRule= new PriceWarningRule(new BigDecimal("1000"));
        PriceWarningRule higherThresholdRule= new PriceWarningRule(new BigDecimal("1500"));
        boolean result = lowerThresholdRule.shouldWarn(new BigDecimal("1200"));
        boolean result2 = higherThresholdRule.shouldWarn(new BigDecimal("1200"));
        assertTrue(result);
        assertFalse(result2);
    }
}
