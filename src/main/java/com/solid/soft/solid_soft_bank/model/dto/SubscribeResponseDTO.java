package com.solid.soft.solid_soft_bank.model.dto;

public class SubscribeResponseDTO {

    private String solidTransactionCode;
    private String message;
    private boolean isSubscribe;

    public SubscribeResponseDTO(final String solidTransactionCode, final String message, boolean isSubscribe) {
        this.solidTransactionCode = solidTransactionCode;
        this.message = message;
        this.isSubscribe= isSubscribe;
    }

    public SubscribeResponseDTO() {
    }

    public String getSolidTransactionCode() {
        return solidTransactionCode;
    }

    public void setSolidTransactionCode(final String transactionCode) {
        this.solidTransactionCode = transactionCode;
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
