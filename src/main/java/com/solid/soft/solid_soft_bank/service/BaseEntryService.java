package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseEntryService {

    private static final Logger log = LoggerFactory.getLogger(BaseEntryService.class);

    public String setErrorResponse(String message) {
        log.warn("Validation failed: {}", message);
        return message;
    }

    public PaymentTransactionEntryEntity createEntry(final Double amount,
                                                      final String currency,
                                                      final String failedRedirectURL,
                                                      final String successRedirectURL,
                                                      final Long paymentTransactionId,
                                                      final String resultMessage,
                                                      final boolean status,
                                                      PaymentTransactionType type) {
        final PaymentTransactionEntryEntity authenticateEntry = new PaymentTransactionEntryEntity();
        authenticateEntry.setAmount(amount);
        authenticateEntry.setCurrency(currency);
        authenticateEntry.setFailedRedirectURL(failedRedirectURL);
        authenticateEntry.setSuccessRedirectURL(successRedirectURL);
        authenticateEntry.setResultMessage(resultMessage);
        authenticateEntry.setTransactionType(type);
        authenticateEntry.setStatus(status);
        authenticateEntry.setPaymentTransactionId(paymentTransactionId);
        return authenticateEntry;
    }
}
