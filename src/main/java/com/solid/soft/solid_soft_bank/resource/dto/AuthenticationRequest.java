package com.solid.soft.solid_soft_bank.resource.dto;

import com.solid.soft.solid_soft_bank.model.dto.CardDTO;

public class AuthenticationRequest {

    private String merchantTransactionCode;
    private String apiKey;
    private String bankTransactionCode;
    private double amount;
    private String currency;
    private CardDTO card;
    private boolean isExternalProcess;

    public AuthenticationRequest(String merchantTransactionCode, String apiKey, String bankTransactionCode, double amount, String currency, CardDTO card, boolean isExternalProcess) {
        this.merchantTransactionCode = merchantTransactionCode;
        this.apiKey = apiKey;
        this.bankTransactionCode = bankTransactionCode;
        this.amount = amount;
        this.currency = currency;
        this.card = card;
        this.isExternalProcess = isExternalProcess;
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

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(final CardDTO card) {
        this.card = card;
    }

    public boolean isExternalProcess() {
        return isExternalProcess;
    }

    public void setExternalProcess(boolean externalProcess) {
        isExternalProcess = externalProcess;
    }
}
