package com.solid.soft.solid_soft_bank.service.dto;

import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;

public class CheckoutPageData {
    private CardDTO card;
    private MerchantDTO merchant;
    private boolean status;

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public MerchantDTO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDTO merchant) {
        this.merchant = merchant;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
