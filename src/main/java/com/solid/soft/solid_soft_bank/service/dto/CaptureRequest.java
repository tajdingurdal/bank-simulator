package com.solid.soft.solid_soft_bank.service.dto;

import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;

public class CaptureRequest {

    private String bankTransactionCode;
    private String cardNo;
    private Double amount;
    private MerchantDTO merchant;

    public CaptureRequest(String bankTransactionCode, String cardNo, Double amount, MerchantDTO merchant) {
        this.bankTransactionCode = bankTransactionCode;
        this.cardNo = cardNo;
        this.amount = amount;
        this.merchant = merchant;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public MerchantDTO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDTO merchant) {
        this.merchant = merchant;
    }
}
