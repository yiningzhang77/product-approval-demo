package com.example.productapproval.repository;

import com.example.productapproval.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    List<Merchant> findAllByEnabledOrderByIdAsc(Boolean enabled);

    List<Merchant> findAllByOrderByIdAsc();
}
