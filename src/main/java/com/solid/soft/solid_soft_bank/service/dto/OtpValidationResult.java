package com.solid.soft.solid_soft_bank.service.dto;

import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;

public class OtpValidationResult {

    private boolean isValid;
    public Double amount;
    private MerchantDTO merchant;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
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
