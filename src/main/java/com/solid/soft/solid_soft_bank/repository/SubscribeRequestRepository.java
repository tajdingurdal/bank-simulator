package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.SubscribeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscribeRequestRepository extends JpaRepository<SubscribeRequestEntity, Long> {

    Optional<SubscribeRequestEntity> findByMerchantTransactionCode(String merchantTransactionCode);
}
