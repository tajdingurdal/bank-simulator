package com.solid.soft.solid_soft_bank.model;

import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_transaction")
public class PaymentTransactionEntryEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private PaymentTransactionType transactionType;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "amount")
    private String amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_transaction_id", nullable = false)
    private Long paymentTransactionId;

    @ManyToOne
    @JoinColumn(name = "payment_transaction_id", nullable = false, updatable = false)
    private PaymentTransactionEntity paymentTransaction;


}
