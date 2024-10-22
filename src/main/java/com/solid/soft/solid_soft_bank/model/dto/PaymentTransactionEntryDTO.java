package com.solid.soft.solid_soft_bank.model.dto;

import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;

import java.time.ZonedDateTime;

public class PaymentTransactionEntryDTO extends BaseDTO{

    private PaymentTransactionType transactionType;

    private boolean status;

    private String resultMessage;

    private String successRedirectURL;

    private String failedRedirectURL;

    private Double amount;

    private String currency;

    private ZonedDateTime createDate;

    private Long paymentTransactionId;

    private PaymentTransactionDTO paymentTransactionDto;

    public PaymentTransactionEntryDTO() {
    }

    public PaymentTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final PaymentTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(final boolean status) {
        this.status = status;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(final Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public PaymentTransactionDTO getPaymentTransactionDto() {
        return paymentTransactionDto;
    }

    public void setPaymentTransactionDto(final PaymentTransactionDTO paymentTransactionDto) {
        this.paymentTransactionDto = paymentTransactionDto;
    }
}
