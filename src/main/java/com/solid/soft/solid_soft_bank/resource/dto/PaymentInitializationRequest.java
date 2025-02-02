package com.solid.soft.solid_soft_bank.resource.dto;

public class PaymentInitializationRequest {

    private String merchantTransactionCode;
    private String apiKey;
    private Double amount;
    private String currency;

    public PaymentInitializationRequest() {
    }

    public PaymentInitializationRequest(String merchantTransactionCode, String apiKey, Double amount, String currency) {
        this.merchantTransactionCode = merchantTransactionCode;
        this.apiKey = apiKey;
        this.amount = amount;
        this.currency = currency;
    }

    public String getMerchantTransactionCode() {
        return merchantTransactionCode;
    }

    public void setMerchantTransactionCode(String merchantTransactionCode) {
        this.merchantTransactionCode = merchantTransactionCode;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
