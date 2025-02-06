package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionEntryRepository;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionRepository;
import com.solid.soft.solid_soft_bank.resource.dto.PaymentInitializationRequest;
import com.solid.soft.solid_soft_bank.resource.dto.PaymentInitializationResponse;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentInitializationService extends BaseEntryService {

    private final PaymentTransactionService paymentTransactionService;
    Logger log = LoggerFactory.getLogger(PaymentInitializationService.class);

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final MerchantService merchantService;
    private final PaymentTransactionEntryRepository entryRepository;

    private final List<String> currencies = List.of("USD", "TRY", "EUR", "GBP", "JPY");


    public PaymentInitializationService(final MerchantService merchantService,
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

    public PaymentTransactionEntryDTO findSubscribeEntryByBankTransactionCode(String bankTransactionCode) {
        return paymentTransactionService.findByBankTransactionCodeAndType(bankTransactionCode, PaymentTransactionType.SUBSCRIBE);
    }

    public PaymentInitializationResponse initializePayment(final PaymentInitializationRequest request) throws IllegalStateException {

        String merchantTransactionCode = request.getMerchantTransactionCode();
        Double amount = request.getAmount();
        String currency = request.getCurrency();
        String apiKey = request.getApiKey();

        log.debug("Starting subscription process: Merchant Transaction Code: {}, API Key: {}, Amount: {}, Currency: {}", merchantTransactionCode, apiKey, amount, currency);

        final String validationResult = validateSubscribe(merchantTransactionCode, apiKey, amount, currency);

        if (validationResult != null) {
            return generateFailedSubscriptionTransaction(merchantTransactionCode, apiKey, amount, currency, validationResult);
        }

        return generateSuccessSubscriptionTransaction(merchantTransactionCode, apiKey, amount, currency);
    }

    private PaymentInitializationResponse generateSuccessSubscriptionTransaction(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) {
        final String bankTransactionCode = createBankTransactionCode(merchantTransactionCode);
        final boolean subscribe = true;

        final MerchantDTO merchantDTO = merchantService.findByApikey(apiKey);

        if (merchantDTO == null) {
            throw new IllegalStateException("Merchant not found by API key: " + apiKey);
        }

        // Save Payment Transaction Entity
        final PaymentTransactionEntity transaction = new PaymentTransactionEntity();
        transaction.setMerchantTransactionCode(merchantTransactionCode);
        transaction.setMerchantId(merchantDTO.getId());
        transaction.setBankTransactionCode(bankTransactionCode);
        final PaymentTransactionEntity savedTransaction = paymentTransactionRepository.save(transaction);

        // Save Subscribe
        final PaymentTransactionEntryEntity entry = createEntry(amount, currency, savedTransaction.getId(), ResponseMessages.SUBSCRIBE_SUCCESS,
                subscribe, null, PaymentTransactionType.SUBSCRIBE);
        final PaymentTransactionEntryEntity savedPaymentTransactionEntryEntity = entryRepository.save(entry);

        // Return
        final PaymentInitializationResponse response = new PaymentInitializationResponse();
        response.setId(savedPaymentTransactionEntryEntity.getId());
        response.setSubscribe(subscribe);
        response.setMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);

        log.debug("Subscription process completed successfully: {}", response);

        return response;
    }

    private PaymentInitializationResponse generateFailedSubscriptionTransaction(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency, final String validationResult) {
        log.warn("Subscription validation failed: {}", validationResult);

        // Save Payment Transaction Entity
        final PaymentTransactionEntity transaction = new PaymentTransactionEntity();
        transaction.setMerchantTransactionCode(merchantTransactionCode);
        transaction.setBankTransactionCode(null);
        transaction.setMerchantId(merchantService.findByApikey(apiKey).getId());
        final PaymentTransactionEntity savedTransaction = paymentTransactionService.savePaymentTransaction(transaction);

        // Save Subscribe Entry
        final PaymentTransactionEntryEntity entry = createEntry(amount, currency, savedTransaction.getId(), validationResult,
                false, null, PaymentTransactionType.SUBSCRIBE);
        final PaymentTransactionEntryEntity savedEntry = entryRepository.save(entry);

        // Return
        final PaymentInitializationResponse response = new PaymentInitializationResponse();
        response.setId(savedEntry.getId());
        response.setMessage(validationResult);
        response.setSubscribe(false);
        response.setBankTransactionCode(null);

        log.debug("Returning subscription response due to validation failure: {}", response);
        return response;
    }

    private String validateSubscribe(final String merchantTransactionCode, final String apiKey, final Double amount, final String currency) {


        if (StringUtils.isEmpty(merchantTransactionCode) || StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(amount) || StringUtils.isEmpty(currency)) {
            return setErrorResponse(ResponseMessages.nullParameterMessage(merchantTransactionCode, apiKey, amount, currency));
        }

        final MerchantDTO merchantDto = merchantService.findByApikey(apiKey);
        if (merchantDto == null) {
            return setErrorResponse(ResponseMessages.merchantNotFoundByApiKey(apiKey));
        }

        if (!merchantDto.getApiKey().equals(apiKey)) {
            return setErrorResponse(ResponseMessages.invalidApiKeyMessage(apiKey));
        }

        if (findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode) != null) {
            return setErrorResponse(ResponseMessages.transactionCodeAlreadySubscribedMessage(merchantTransactionCode));
        }

        if (amount < 0 || amount > 1000000) {
            return setErrorResponse(ResponseMessages.invalidAmountMessage(amount));
        }

        if (!currencies.contains(currency)) {
            return setErrorResponse(ResponseMessages.invalidCurrencyMessage(currency));
        }

        return null;
    }

    private String createBankTransactionCode(final String merchantTransactionCode) {
        return String.format(merchantTransactionCode.toLowerCase() + UUID.randomUUID().toString().toLowerCase() + ZonedDateTime.now().toInstant().toEpochMilli());
    }
}
