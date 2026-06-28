package com.example.productapproval.service;

import com.example.productapproval.entity.Merchant;
import com.example.productapproval.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Transactional(readOnly = true)
    public List<Merchant> findAll() {
        return merchantRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Merchant> findEnabled() {
        return merchantRepository.findAllByEnabledOrderByIdAsc(true);
    }

    @Transactional(readOnly = true)
    public Merchant requireEnabled(Long merchantId) {
        if (merchantId == null) {
            throw new IllegalArgumentException("缺少商家ID，请通过正确入口访问商家端");
        }
        return merchantRepository.findById(merchantId)
                .filter(item -> Boolean.TRUE.equals(item.getEnabled()))
                .orElseThrow(() -> new IllegalArgumentException("商家不存在或已停用"));
    }
}
