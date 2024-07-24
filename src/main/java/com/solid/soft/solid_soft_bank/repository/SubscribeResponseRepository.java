package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscribeResponseRepository extends JpaRepository<SubscribeResponseEntity, Long> {

    Optional<SubscribeResponseEntity> findByMerchantTransactionCode(String merchantTransactionCode);
}
