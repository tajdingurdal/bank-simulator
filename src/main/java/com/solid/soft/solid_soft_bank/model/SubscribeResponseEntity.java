package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscribe_response")
public class SubscribeResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "merchant_transaction_code")
    private String merchantTransactionCode;

    @Column(name = "solid_bank_transaction_code")
    private String solidBankTransactionCode;

    @Column(name = "result_message")
    private String resultMessage;

    @Column(name = "subscribe")
    private boolean isSubscribe;

    public SubscribeResponseEntity(final String merchantTransactionCode, final String solidBankTransactionCode, final String resultMessage, final boolean isSubscribe) {
        this.merchantTransactionCode = merchantTransactionCode;
        this.solidBankTransactionCode = solidBankTransactionCode;
        this.resultMessage = resultMessage;
        this.isSubscribe = isSubscribe;
    }

    public SubscribeResponseEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getMerchantTransactionCode() {
        return merchantTransactionCode;
    }

    public void setMerchantTransactionCode(final String merchantTransactionCode) {
        this.merchantTransactionCode = merchantTransactionCode;
    }

    public String getSolidBankTransactionCode() {
        return solidBankTransactionCode;
    }

    public void setSolidBankTransactionCode(final String solidBankTransactionCode) {
        this.solidBankTransactionCode = solidBankTransactionCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(final boolean subscribe) {
        isSubscribe = subscribe;
    }
}
