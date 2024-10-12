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
import java.util.UUID;

@Service
public class SubscribeService {

    private final PaymentTransactionService paymentTransactionService;
    Logger log = LoggerFactory.getLogger(SubscribeService.class);

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final MerchantService merchantService;
    private final PaymentTransactionEntryRepository entryRepository;

    private final List<String> currencies = List.of("USD", "TRY", "EURO");


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

    public SubscribeResponseDTO subscribe(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) throws IllegalStateException {

        log.debug("Starting subscription process: Merchant Transaction Code: {}, API Key: {}, Amount: {}, Currency: {}",
                merchantTransactionCode, apiKey, amount, currency);

        final SubscribeResponseDTO response = new SubscribeResponseDTO();
        final String validationResult = validateSubscribe(merchantTransactionCode, apiKey, amount, currency);

        if (validationResult != null) {
            log.warn("Subscription validation failed: {}", validationResult);

            final PaymentTransactionEntryEntity subscribeEntry = new PaymentTransactionEntryEntity();
            final PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();

            paymentTransactionEntity.setMerchantTransactionCode(merchantTransactionCode);
            paymentTransactionEntity.setBankTransactionCode(null);
            paymentTransactionEntity.setMerchandId(merchantService.findByApikey(apiKey).getId());
            final PaymentTransactionEntity savedPaymentTransactionEntity = paymentTransactionService.savePaymentTransaction(paymentTransactionEntity);

            subscribeEntry.setTransactionType(PaymentTransactionType.SUBSCRIBE);
            subscribeEntry.setStatus(false);
            subscribeEntry.setResultMessage(validationResult);
            subscribeEntry.setSuccessRedirectURL(null);
            subscribeEntry.setFailedRedirectURL(null);
            subscribeEntry.setAmount(amount);
            subscribeEntry.setCurrency(currency);
            subscribeEntry.setCreateDate(ZonedDateTime.now());
            subscribeEntry.setPaymentTransactionId(savedPaymentTransactionEntity.getId());
            final PaymentTransactionEntryEntity savedPaymentTransactionEntryEntity = entryRepository.save(subscribeEntry);

            response.setId(savedPaymentTransactionEntryEntity.getId());
            response.setMessage(validationResult);
            response.setSubscribe(false);
            response.setBankTransactionCode(null);

            log.debug("Returning subscription response due to validation failure: {}", response);
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

        final PaymentTransactionEntryEntity savedPaymentTransactionEntryEntity = entryRepository.save(subscribeEntity);

        // Return
        response.setId(savedPaymentTransactionEntryEntity.getId());
        response.setSubscribe(subscribe);
        response.setMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);

        log.debug("Subscription process completed successfully: {}", response);

        return response;
    }

    private String validateSubscribe(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) {


        if (merchantTransactionCode == null || apiKey == null || amount == null || currency == null) {
            final String message = ResponseMessages.nullParameterMessage(merchantTransactionCode, apiKey, amount, currency);
            log.warn("Validation failed: {}", message);
            return message;
        }

        final MerchantDTO merchantDto = merchantService.findByApikey(apiKey);
        if (merchantDto == null) {
            String message = ResponseMessages.merchantNotFoundByApiKey(apiKey);
            log.warn("Validation failed: {}", message);
            return message;
        }

        if (!merchantDto.getApiKey().equals(apiKey)) {
            String message = ResponseMessages.invalidApiKeyMessage(apiKey);
            log.warn("Validation failed: {}", message);
            return message;
        }

        if (findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode) != null) {
            String message = ResponseMessages.transactionCodeAlreadySubscribedMessage(merchantTransactionCode);
            log.warn("Validation failed: {}", message);
            return message;
        }

        if (amount < 0 || amount > 1000000) {
            String message = ResponseMessages.invalidAmountMessage(amount);
            log.warn("Validation failed: {}", message);
        }

        if (!currencies.contains(currency)) {
            String message = ResponseMessages.invalidCurrencyMessage(currency);
            log.warn("Validation failed: {}", message);
            return message;
        }

        return null;
    }

    private String createBankTransactionCode(final String merchantTransactionCode) {
        return String.format(
                merchantTransactionCode.toLowerCase() + UUID.randomUUID().toString().toLowerCase() + ZonedDateTime.now().toInstant().toEpochMilli());
    }

}
