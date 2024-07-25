package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.dto.SubscribeResponseDTO;
import com.solid.soft.solid_soft_bank.repository.SubscribeRequestRepository;
import com.solid.soft.solid_soft_bank.repository.SubscribeResponseRepository;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubscribeService {

    private final MerchantService merchantService;
    Logger log = LoggerFactory.getLogger(SubscribeService.class);

    private final List<String> currencies = List.of("USD", "TL", "EURO");

    private final SubscribeRequestRepository  subscribeRequestRepository;
    private final SubscribeResponseRepository subscribeResponseRepository;

    public SubscribeService(final SubscribeRequestRepository subscribeRequestRepository,
                            final SubscribeResponseRepository subscribeResponseRepository, final MerchantService merchantService) {
        this.subscribeRequestRepository = subscribeRequestRepository;
        this.subscribeResponseRepository = subscribeResponseRepository;
        this.merchantService = merchantService;
    }

    public SubscribeResponseEntity findSubscribeResponseByMerchantTransactionCode(String merchantTransactionCode) {
        return subscribeResponseRepository.findByMerchantTransactionCode(merchantTransactionCode).orElseThrow();
    }

    public SubscribeRequestEntity findSubscribeRequestByMerchantTransactionCode(String merchantTransactionCode) {
        return subscribeRequestRepository.findByMerchantTransactionCode(merchantTransactionCode).orElseThrow();
    }

    public SubscribeResponseDTO subscribe(final String merchantTransactionCode, final String apiKey, final String amount,
                                          final String currency) {
        final SubscribeResponseDTO response         = new SubscribeResponseDTO();
        final String               validationResult = validateSubscribe(merchantTransactionCode, apiKey, amount, currency);
        if (validationResult != null) {
            response.setMessage(validationResult);
            response.setSubscribe(false);
            return response;
        }

        final SubscribeRequestEntity subscribeRequestEntity = new SubscribeRequestEntity(merchantTransactionCode, apiKey, amount, currency);
        subscribeRequestRepository.save(subscribeRequestEntity);

        final String  bankTransactionCode = String.format(merchantTransactionCode.toLowerCase() + UUID.randomUUID() + ZonedDateTime.now().toInstant().toEpochMilli());
        final boolean subscribe                = true;


        final SubscribeResponseEntity responseEntity = new SubscribeResponseEntity(merchantTransactionCode,
                                                                                   bankTransactionCode,
                                                                                   ResponseMessages.SUBSCRIBE_SUCCESS,
                                                                                   subscribe);
        subscribeResponseRepository.save(responseEntity);

        response.setSubscribe(subscribe);
        response.setMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);
        return response;
    }

    private String validateSubscribe(final String merchantTransactionCode, final String apiKey, final String amount,
                                     final String currency) {

        if (merchantTransactionCode == null || apiKey == null || amount == null || currency == null) {
            return ResponseMessages.nullParameterMessage(merchantTransactionCode, apiKey, amount, currency);
        }

        final MerchantEntity merchantEntity = merchantService.findByApikey(apiKey);
        if (!merchantEntity.getApiKey().equals(apiKey)) {
            return ResponseMessages.invalidApiKeyMessage(apiKey);
        }

        if (merchantTransactionCode.length() != 16) {
            return ResponseMessages.invalidTransactionCodeLengthMessage(merchantTransactionCode);
        }

        if (subscribeRequestRepository.findByMerchantTransactionCode(merchantTransactionCode).isPresent()) {
            return ResponseMessages.transactionCodeAlreadySubscribedMessage(merchantTransactionCode);
        }

        final long amountLong = Long.parseLong(amount);
        if (amountLong < 0 || amountLong > 1000000) {
            return ResponseMessages.invalidAmountMessage(amountLong);
        }

        if (!currencies.contains(currency)) {
            return ResponseMessages.invalidCurrencyMessage(currency);
        }

        return null;
    }
}
