package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_payment_process")
public class UserPaymentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "bank_transaction_code")
    private String bankTransactionCode;

    @Column(name = "card_id")
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    public UserPaymentProcess() {
    }

    public UserPaymentProcess(final Double amount, final String currency, String merchantName, String bankTransactionCode) {
        this.merchantName = merchantName;
        this.bankTransactionCode = bankTransactionCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(final String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", bankTransactionCode='" + bankTransactionCode + '\'' +
                '}';
    }
}
