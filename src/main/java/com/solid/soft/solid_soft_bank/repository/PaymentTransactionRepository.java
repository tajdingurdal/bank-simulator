package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long> {

    Optional<PaymentTransactionEntity> findByBankTransactionCode(String bankTransactionCode);

    Optional<PaymentTransactionEntity> findByMerchantTransactionCode(String merchantTransactionCode);

    Optional<PaymentTransactionEntity> findByMerchandId(Long merchandId);
}
