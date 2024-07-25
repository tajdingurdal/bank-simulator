package com.solid.soft.solid_soft_bank.model.dto;

public class SubscribeResponseDTO {

    private String bankTransactionCode;
    private String message;
    private boolean isSubscribe;

    public SubscribeResponseDTO(final String bankTransactionCode, final String message, boolean isSubscribe) {
        this.bankTransactionCode = bankTransactionCode;
        this.message = message;
        this.isSubscribe= isSubscribe;
    }

    public SubscribeResponseDTO() {
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
}
