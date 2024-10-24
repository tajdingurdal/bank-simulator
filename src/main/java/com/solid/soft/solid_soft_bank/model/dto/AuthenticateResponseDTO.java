package com.solid.soft.solid_soft_bank.model.dto;

import java.time.Instant;

public class AuthenticateResponseDTO extends BaseDTO{

    private String  bankTransactionCode;
    private boolean status;
    private String  message;
    private String url;
    private boolean otpRequired = true;

    public AuthenticateResponseDTO() {
        super();
    }

    public AuthenticateResponseDTO(Long id, final String bankTransactionCode, final boolean status, final String message,
                                   final String url) {
        super(id, Instant.now(), Instant.now());
        this.bankTransactionCode = bankTransactionCode;
        this.status = status;
        this.message = message;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isOtpRequired() {
        return otpRequired;
    }

    public void setOtpRequired(final boolean otpRequired) {
        this.otpRequired = otpRequired;
    }
}
