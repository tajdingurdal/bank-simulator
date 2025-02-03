package com.solid.soft.solid_soft_bank.resource.dto;

import com.solid.soft.solid_soft_bank.model.dto.BaseDTO;

public class AuthenticationResponse extends BaseDTO {

    private String  bankTransactionCode;
    private boolean status;
    private String  message;
    private String url;
    private boolean otpRequired;

    public AuthenticationResponse() {
        super();
    }

    public AuthenticationResponse(final Long id,
                                  final String bankTransactionCode,
                                  final boolean status,
                                  final String message,
                                  final String url,
                                  final boolean otpRequired) {
        super(id);
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
