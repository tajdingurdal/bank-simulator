package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTransactionEntryRepository extends JpaRepository<PaymentTransactionEntryEntity, Long> {

    Optional<PaymentTransactionEntryEntity> findByPaymentTransactionIdAndTransactionType(Long paymentTransactionId, PaymentTransactionType transactionType);

    Optional<PaymentTransactionEntryEntity> findByIdAndTransactionType(Long id, PaymentTransactionType transactionType);

    @Query("SELECT pte FROM PaymentTransactionEntryEntity pte " +
            "INNER JOIN pte.paymentTransaction pt " +
            "WHERE pte.paymentTransaction.merchantTransactionCode =:merchantTransactionCode " +
            "AND pte.transactionType = :transactionType")
    Optional<PaymentTransactionEntryEntity> findByMerchantTransactionCodeAndTransactionType(@Param("merchantTransactionCode") String merchantTransactionCode, @Param("transactionType") PaymentTransactionType transactionType);


    @Query("SELECT pte FROM PaymentTransactionEntryEntity pte " +
            "INNER JOIN pte.paymentTransaction pt " +
            "WHERE pt.bankTransactionCode = :bankTransactionCode " +
            "AND pte.transactionType = :transactionType")
    Optional<PaymentTransactionEntryEntity> findByBankTransactionCodeAndTransactionType(@Param("bankTransactionCode") final String bankTransactionCode,
                                                                                        @Param("transactionType") final PaymentTransactionType transactionType);

}
