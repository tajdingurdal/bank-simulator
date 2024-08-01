package com.solid.soft.solid_soft_bank.model.dto;

public class SubscribeResponseDTO {

    private Long id;
    private String bankTransactionCode;
    private String message;
    private boolean isSubscribe;

    public SubscribeResponseDTO(final Long id, final String bankTransactionCode, final String message, boolean isSubscribe) {
        this.id = id;
        this.bankTransactionCode = bankTransactionCode;
        this.message = message;
        this.isSubscribe = isSubscribe;
    }

    public SubscribeResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String transactionCode) {
        this.bankTransactionCode = transactionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(final boolean subscribe) {
        isSubscribe = subscribe;
    }

    @Override
    public String toString() {
        return "SubscribeResponseDTO{" +
                "id=" + id +
                ", bankTransactionCode='" + bankTransactionCode + '\'' +
                ", message='" + message + '\'' +
                ", isSubscribe=" + isSubscribe +
                '}';
    }
}
