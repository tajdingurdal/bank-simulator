package com.solid.soft.solid_soft_bank.model;

import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_transaction_entry")
public class PaymentTransactionEntryEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private PaymentTransactionType transactionType;

    @Column(name = "status")
    private boolean status;

    @Column(name = "result_message")
    private String resultMessage;

    @Column(name = "success_redirect_url")
    private String successRedirectURL;

    @Column(name = "failed_redirect_url")
    private String failedRedirectURL;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_transaction_id", nullable = false)
    private Long paymentTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_transaction_id", updatable = false, insertable = false)
    private PaymentTransactionEntity paymentTransaction;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public Long getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(final Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public PaymentTransactionEntity getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(final PaymentTransactionEntity paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }
}
