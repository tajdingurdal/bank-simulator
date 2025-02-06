package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationRequest;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationResponse;
import com.solid.soft.solid_soft_bank.service.dto.CheckoutPageData;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;

@Service
public class PaymentWebService {

    private final PaymentAuthenticationService authService;
    private final PaymentInitializationService initializationService;

    public PaymentWebService(PaymentAuthenticationService authService, PaymentInitializationService initializationService) {
        this.authService = authService;
        this.initializationService = initializationService;
    }

    public CheckoutPageData prepareShowPaymentForm(String bankTransactionCode) throws InstanceAlreadyExistsException {
        CheckoutPageData pageData = new CheckoutPageData();
        PaymentTransactionEntryDTO subscribeEntry = initializationService.findSubscribeEntryByBankTransactionCode(bankTransactionCode);
        PaymentTransactionDTO paymentTransactionDto = subscribeEntry.getPaymentTransactionDto();
        MerchantDTO merchantDTO = paymentTransactionDto.getMerchantDTO();

        final CardDTO card = new CardDTO();
        card.setAmount(subscribeEntry.getAmount());
        card.setCurrency(subscribeEntry.getCurrency());

        final AuthenticationResponse authResult = authService.authenticatePrePayment(
                new AuthenticationRequest(
                        paymentTransactionDto.getMerchantTransactionCode(),
                        merchantDTO.getApiKey(),
                        bankTransactionCode,
                        subscribeEntry.getAmount(),
                        subscribeEntry.getCurrency(),
                        card,
                        false
                )
        );

        pageData.setMerchant(merchantDTO);

        if (!subscribeEntry.isStatus() || !authResult.isStatus()) {
            pageData.setStatus(false);
            return pageData;
        }

        pageData.setStatus(true);
        pageData.setCard(card);
        return pageData;
    }

}
