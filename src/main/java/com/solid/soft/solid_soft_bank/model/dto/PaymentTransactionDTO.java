package com.solid.soft.solid_soft_bank.model.dto;

public class PaymentTransactionDTO extends BaseDTO{

    private String bankTransactionCode;

    private String merchantTransactionCode;

    private Long merchandId;

    private MerchantDTO merchantEntity;

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

    public Long getMerchandId() {
        return merchandId;
    }

    public void setMerchandId(final Long merchandId) {
        this.merchandId = merchandId;
    }

    public MerchantDTO getMerchantEntity() {
        return merchantEntity;
    }

    public void setMerchantEntity(final MerchantDTO merchantEntity) {
        this.merchantEntity = merchantEntity;
    }
}
