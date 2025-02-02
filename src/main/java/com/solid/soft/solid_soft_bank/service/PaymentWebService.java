package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.service.dto.CheckoutPageData;
import org.springframework.stereotype.Service;

@Service
public class PaymentWebService {

    private final PaymentAuthenticationService authService;
    private final PaymentInitializationService paymentInitializationService;

    public PaymentWebService(PaymentAuthenticationService authService, PaymentInitializationService paymentInitializationService) {
        this.authService = authService;
        this.paymentInitializationService = paymentInitializationService;
    }

    public CheckoutPageData preparePaymentForm(String bankTransactionCode) {
        CheckoutPageData pageData = new CheckoutPageData();

        final PaymentTransactionEntryDTO authenticateEntry = authService.findByBankTransactionCode(bankTransactionCode);
        PaymentTransactionDTO paymentTransactionDto = authenticateEntry.getPaymentTransactionDto();
        final PaymentTransactionEntryDTO subscribeEntry = paymentInitializationService.findSubscribeEntryByMerchantTransactionCode(paymentTransactionDto.getMerchantTransactionCode());

        if (!subscribeEntry.isStatus() || !authenticateEntry.isStatus()) {
            pageData.setStatus(false);
        }

        final CardDTO card = new CardDTO();
        card.setAmount(authenticateEntry.getAmount());
        card.setCurrency(authenticateEntry.getCurrency());
        pageData.setStatus(true);
        pageData.setCard(card);
        pageData.setMerchant(paymentTransactionDto.getMerchantEntity());
        return pageData;
    }

}
