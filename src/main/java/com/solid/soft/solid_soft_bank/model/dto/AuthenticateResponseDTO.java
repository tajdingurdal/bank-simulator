package com.solid.soft.solid_soft_bank.model.dto;

public class AuthenticateResponseDTO {

    private String  solidBankTransactionCode;
    private boolean status;
    private String  message;
    private String  paymentUrl;

    public AuthenticateResponseDTO() {
    }

    public AuthenticateResponseDTO(final String solidBankTransactionCode, final boolean status, final String message,
                                   final String paymentUrl) {
        this.solidBankTransactionCode = solidBankTransactionCode;
        this.status = status;
        this.message = message;
        this.paymentUrl = paymentUrl;
    }

    public String getSolidBankTransactionCode() {
        return solidBankTransactionCode;
    }

    public void setSolidBankTransactionCode(final String solidBankTransactionCode) {
        this.solidBankTransactionCode = solidBankTransactionCode;
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
