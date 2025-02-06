package com.solid.soft.solid_soft_bank.model.dto;

import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;

/**
 * A DTO of {{@link com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity}}
 */
public class PaymentTransactionEntryDTO extends BaseDTO {

    private PaymentTransactionType transactionType;

    private boolean status;

    private Boolean isExternalProcess;

    private String resultMessage;

    private Double amount;

    private String currency;

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

    public Boolean getExternalProcess() {
        return isExternalProcess;
    }

    public void setExternalProcess(Boolean externalProcess) {
        isExternalProcess = externalProcess;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
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
