package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pre_checkout")
public class PreCheckoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "merchant_transaction_code")
    private String merchantTransactionCode;

    @Column(name = "solid_bank_transaction_code")
    private String solidBankTransactionCode;

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

    public PreCheckoutEntity() {
    }

    public PreCheckoutEntity(final double amount, final String failedRedirectURL, final String successRedirectURL, final String currency,
                             final String resultMessage, final String solidBankTransactionCode, final String merchantTransactionCode) {
        this.amount = amount;
        this.failedRedirectURL = failedRedirectURL;
        this.successRedirectURL = successRedirectURL;
        this.currency = currency;
        this.resultMessage = resultMessage;
        this.solidBankTransactionCode = solidBankTransactionCode;
        this.merchantTransactionCode = merchantTransactionCode;
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
}
