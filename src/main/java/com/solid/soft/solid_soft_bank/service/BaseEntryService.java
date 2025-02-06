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
                                                      final Long paymentTransactionId,
                                                      final String resultMessage,
                                                      final boolean status,
                                                      final Boolean isExternal,
                                                      PaymentTransactionType type) {
        final PaymentTransactionEntryEntity authenticateEntry = new PaymentTransactionEntryEntity();
        authenticateEntry.setAmount(amount);
        authenticateEntry.setCurrency(currency);
        authenticateEntry.setResultMessage(resultMessage);
        authenticateEntry.setTransactionType(type);
        authenticateEntry.setStatus(status);
        authenticateEntry.setExternalProcess(isExternal);
        authenticateEntry.setPaymentTransactionId(paymentTransactionId);
        return authenticateEntry;
    }
}
