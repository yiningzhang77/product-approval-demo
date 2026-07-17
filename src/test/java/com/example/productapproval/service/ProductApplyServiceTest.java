package com.example.productapproval.service;

import com.example.productapproval.dto.ProductApplyForm;
import com.example.productapproval.entity.ProductApply;
import com.example.productapproval.repository.ProductApplyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ProductApplyServiceTest {
    @Autowired
    private ProductApplyService productApplyService;
    @Autowired
    private ProductApplyRepository productApplyRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPersistCompleteWarningDecisionContext() {
        //Given
        ProductApplyForm form = new ProductApplyForm();
        form.setCategoryId(1L);
        form.setProductName("测试商品");
        form.setPrice(new BigDecimal("1200"));

        //When
        ProductApply created = productApplyService.create(1L, form);

        entityManager.flush();
        entityManager.clear();

        ProductApply persisted = productApplyRepository.findById(created.getId()).orElseThrow();

        //Then
        assertTrue(persisted.getWarning());
        assertEquals(0, new BigDecimal("1000").compareTo(persisted.getWarningThreshold()));
        assertTrue(persisted.getWarningReason().contains("1000"));
    }
}
