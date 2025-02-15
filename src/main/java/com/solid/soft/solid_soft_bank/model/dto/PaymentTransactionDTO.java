package com.solid.soft.solid_soft_bank.model.dto;

public class PaymentTransactionDTO extends BaseDTO{

    private String bankTransactionCode;

    private String merchantTransactionCode;

    private Long merchantId;

    private MerchantDTO merchantDTO;

    public PaymentTransactionDTO() {
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    public String getMerchantTransactionCode() {
        return merchantTransactionCode;
    }

    public void setMerchantTransactionCode(final String merchantTransactionCode) {
        this.merchantTransactionCode = merchantTransactionCode;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(final Long merchantId) {
        this.merchantId = merchantId;
    }

    public MerchantDTO getMerchantDTO() {
        return merchantDTO;
    }

    public void setMerchantDTO(final MerchantDTO merchantDTO) {
        this.merchantDTO = merchantDTO;
    }
}
