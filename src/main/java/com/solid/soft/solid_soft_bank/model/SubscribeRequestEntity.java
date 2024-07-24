package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscribe_request")
public class SubscribeRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "merchant_transaction_code")
    private String merchantTransactionCode;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "amount")
    private String amount;

    @Column(name = "currency")
    private String currency;

    public SubscribeRequestEntity(final String merchantTransactionCode, final String apiKey, final String amount, final String currency) {
        this.merchantTransactionCode = merchantTransactionCode;
        this.apiKey = apiKey;
        this.amount = amount;
        this.currency = currency;
    }

    public SubscribeRequestEntity() {

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }
}
