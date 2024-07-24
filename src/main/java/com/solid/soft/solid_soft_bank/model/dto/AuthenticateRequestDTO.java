package com.solid.soft.solid_soft_bank.model.dto;

public class AuthenticateRequestDTO {

    private String merchantTransactionCode;
    private String apiKey;
    private String bankTransactionCode;
    private double amount;
    private String currency;
    private String successRedirectURL;
    private String failureRedirectURL;

    public AuthenticateRequestDTO() {
    }

    public AuthenticateRequestDTO(final String merchantTransactionCode, final String apiKey, final String bankTransactionCode,
                                  final String successRedirectURL,
                                  final String failureRedirectURL, final double amount, final String currency) {
        this.merchantTransactionCode = merchantTransactionCode;
        this.apiKey = apiKey;
        this.bankTransactionCode = bankTransactionCode;
        this.successRedirectURL = successRedirectURL;
        this.failureRedirectURL = failureRedirectURL;
        this.amount = amount;
        this.currency = currency;
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

    public String getSuccessRedirectURL() {
        return successRedirectURL;
    }

    public void setSuccessRedirectURL(final String successRedirectURL) {
        this.successRedirectURL = successRedirectURL;
    }

    public String getFailureRedirectURL() {
        return failureRedirectURL;
    }

    public void setFailureRedirectURL(final String failureRedirectURL) {
        this.failureRedirectURL = failureRedirectURL;
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
}
