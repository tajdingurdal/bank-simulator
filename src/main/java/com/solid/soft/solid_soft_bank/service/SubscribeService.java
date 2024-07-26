package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.dto.SubscribeResponseDTO;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionEntryRepository;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionRepository;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SubscribeService {

    private final PaymentTransactionService paymentTransactionService;
    Logger log = LoggerFactory.getLogger(SubscribeService.class);

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final MerchantService merchantService;
    private final PaymentTransactionEntryRepository entryRepository;

    private final List<String> currencies = List.of("USD", "TL", "EURO");


    public SubscribeService(final MerchantService merchantService,
                            final PaymentTransactionEntryRepository entryRepository,
                            final PaymentTransactionRepository paymentTransactionRepository,
                            final PaymentTransactionService paymentTransactionService) {
        this.merchantService = merchantService;
        this.entryRepository = entryRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionService = paymentTransactionService;
    }

    public PaymentTransactionEntryDTO findSubscribeEntryByMerchantTransactionCode(String merchantTransactionCode) {
        return paymentTransactionService.findByMerchantTransactionCodeAndType(merchantTransactionCode, PaymentTransactionType.SUBSCRIBE);
    }

    public PaymentTransactionEntryDTO findSubscribeEntryByPaymentTransactionId(final Long paymentTransactionId) {
        return paymentTransactionService.findSubscribeEntryByPaymentTransactionIdAndType(paymentTransactionId, PaymentTransactionType.SUBSCRIBE);
    }

    public SubscribeResponseDTO subscribe(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) {

        final SubscribeResponseDTO response = new SubscribeResponseDTO();
        final String validationResult = validateSubscribe(merchantTransactionCode, apiKey, amount, currency);
        if (validationResult != null) {
            response.setMessage(validationResult);
            response.setSubscribe(false);
            return response;
        }

        final String bankTransactionCode = createBankTransactionCode(merchantTransactionCode);
        final boolean subscribe = true;

        final MerchantDTO merchantDTO = merchantService.findByApikey(apiKey);

        // Save Payment Transaction Entity
        final PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
        paymentTransactionEntity.setMerchantTransactionCode(merchantTransactionCode);
        paymentTransactionEntity.setMerchandId(merchantDTO.getId());
        paymentTransactionEntity.setBankTransactionCode(bankTransactionCode);

        final PaymentTransactionEntity savedPaymentTransactionEntity = paymentTransactionRepository.save(paymentTransactionEntity);

        // Save Subscribe
        final PaymentTransactionEntryEntity subscribeEntity = new PaymentTransactionEntryEntity();
        subscribeEntity.setStatus(subscribe);
        subscribeEntity.setAmount(amount);
        subscribeEntity.setCurrency(currency);
        subscribeEntity.setPaymentTransactionId(savedPaymentTransactionEntity.getId());
        subscribeEntity.setResultMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        subscribeEntity.setCreateDate(ZonedDateTime.now());
        subscribeEntity.setTransactionType(PaymentTransactionType.SUBSCRIBE);

        entryRepository.save(subscribeEntity);

        // Return
        response.setSubscribe(subscribe);
        response.setMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);
        return response;
    }

    private String validateSubscribe(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) {

        if (merchantTransactionCode == null || apiKey == null || amount == null || currency == null) {
            return ResponseMessages.nullParameterMessage(merchantTransactionCode, apiKey, amount, currency);
        }

        final MerchantDTO merchantDto = merchantService.findByApikey(apiKey);
        if (merchantDto == null) {
            return ResponseMessages.merchantNotFoundByApiKey(apiKey);
        }

        if (!merchantDto.getApiKey().equals(apiKey)) {
            return ResponseMessages.invalidApiKeyMessage(apiKey);
        }

        if (merchantTransactionCode.length() != 16) {
            return ResponseMessages.invalidTransactionCodeLengthMessage(merchantTransactionCode);
        }

        if (findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode) != null) {
            return ResponseMessages.transactionCodeAlreadySubscribedMessage(merchantTransactionCode);
        }

        if (amount < 0 || amount > 1000000) {
            return ResponseMessages.invalidAmountMessage(amount);
        }

        if (!currencies.contains(currency)) {
            return ResponseMessages.invalidCurrencyMessage(currency);
        }

        return null;
    }

    private String createBankTransactionCode(final String merchantTransactionCode) {
        return String.format(
                merchantTransactionCode.toLowerCase() + UUID.randomUUID().toString().toLowerCase() + ZonedDateTime.now().toInstant().toEpochMilli());
    }

}
