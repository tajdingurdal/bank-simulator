package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "authenticate")
public class AuthenticateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_transaction_code")
    private String merchantTransactionCode;

    @Column(name = "bank_transaction_code")
    private String bankTransactionCode;

    @Column(name = "result_message")
    private String resultMessage;

    @Column(name = "currency")
    private String currency;

    @Column(name = "success_redirect_url")
    private String successRedirectURL;

    @Column(name = "failed_redirect_url")
    private String failedRedirectURL;

    @Column(name = "amount")
    private double amount;

    @Column(name = "authenticate")
    private boolean authenticate;

    @Column(name = "api_key")
    private String apiKey;

    public AuthenticateEntity() {
    }

    public AuthenticateEntity(final double amount, final String failedRedirectURL, final String successRedirectURL, final String currency,
                              final String resultMessage, final String bankTransactionCode, final String merchantTransactionCode,
                              boolean authenticate, String apiKey) {
        this.amount = amount;
        this.failedRedirectURL = failedRedirectURL;
        this.successRedirectURL = successRedirectURL;
        this.currency = currency;
        this.resultMessage = resultMessage;
        this.bankTransactionCode = bankTransactionCode;
        this.merchantTransactionCode = merchantTransactionCode;
        this.authenticate = authenticate;
        this.apiKey = apiKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMerchantTransactionCode() {
        return merchantTransactionCode;
    }

    public void setMerchantTransactionCode(final String merchantTransactionCode) {
        this.merchantTransactionCode = merchantTransactionCode;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String solidBankTransactionCode) {
        this.bankTransactionCode = solidBankTransactionCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getSuccessRedirectURL() {
        return successRedirectURL;
    }

    public void setSuccessRedirectURL(final String successRedirectURL) {
        this.successRedirectURL = successRedirectURL;
    }

    public String getFailedRedirectURL() {
        return failedRedirectURL;
    }

    public void setFailedRedirectURL(final String failedRedirectURL) {
        this.failedRedirectURL = failedRedirectURL;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(final boolean authenticate) {
        this.authenticate = authenticate;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }
}
