package com.solid.soft.solid_soft_bank.model.dto;

public class AuthenticateResponseDTO {

    private String  bankTransactionCode;
    private boolean status;
    private String  message;
    private String  paymentUrl;

    public AuthenticateResponseDTO() {
    }

    public AuthenticateResponseDTO(final String bankTransactionCode, final boolean status, final String message,
                                   final String paymentUrl) {
        this.bankTransactionCode = bankTransactionCode;
        this.status = status;
        this.message = message;
        this.paymentUrl = paymentUrl;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(final boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(final String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
