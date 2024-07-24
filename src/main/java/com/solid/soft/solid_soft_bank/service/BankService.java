package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PreCheckoutEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeRequestEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.model.dto.SubscribeResponseDTO;
import com.solid.soft.solid_soft_bank.repository.PreCheckoutRepository;
import com.solid.soft.solid_soft_bank.repository.SubscribeRequestRepository;
import com.solid.soft.solid_soft_bank.repository.SubscribeResponseRepository;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    private final Logger log = LoggerFactory.getLogger(BankService.class);

    @Value("${solid.bank.api-key}")
    private String apiKey;

    private final List<String> currencies = List.of("USD", "TL", "EURO");

    private final PreCheckoutRepository       preCheckoutRepository;
    private final SubscribeRequestRepository  subscribeRequestRepository;
    private final SubscribeResponseRepository subscribeResponseRepository;

    public BankService(final SubscribeRequestRepository subscribeRequestRepository,
                       final SubscribeResponseRepository subscribeResponseRepository, final PreCheckoutRepository preCheckoutRepository) {
        this.subscribeRequestRepository = subscribeRequestRepository;
        this.subscribeResponseRepository = subscribeResponseRepository;
        this.preCheckoutRepository = preCheckoutRepository;
    }

    public SubscribeResponseDTO subscribe(final String merchantTransactionCode, final String apiKey, final String amount,
                                          final String currency) {
        final SubscribeResponseDTO response    = new SubscribeResponseDTO();
        final String validationResult = validateSubscribe(merchantTransactionCode, apiKey, amount, currency);
        if (validationResult != null) {
            response.setMessage(ResponseMessages.SUBSCRIBE_FAILED);
            response.setSubscribe(false);
            return response;
        }

        final SubscribeRequestEntity subscribeRequestEntity = new SubscribeRequestEntity(merchantTransactionCode, apiKey, amount, currency);
        subscribeRequestRepository.save(subscribeRequestEntity);

        final String  solidBankTransactionCode = UUID.randomUUID().toString() + ZonedDateTime.now().toInstant().toEpochMilli();
        final boolean subscribe                = true;


        final SubscribeResponseEntity responseEntity = new SubscribeResponseEntity(merchantTransactionCode,
                                                                                   solidBankTransactionCode,
                                                                                   ResponseMessages.SUBSCRIBE_SUCCESS,
                                                                                   subscribe);
        subscribeResponseRepository.save(responseEntity);

        response.setSubscribe(subscribe);
        response.setMessage(ResponseMessages.SUBSCRIBE_SUCCESS);
        response.setSolidTransactionCode(solidBankTransactionCode);
        return response;
    }

    public AuthenticateResponseDTO authenticate(final AuthenticateRequestDTO authenticate) {

        final String merchantTransactionCode = authenticate.getMerchantTransactionCode();
        final SubscribeRequestEntity subscribeRequestEntity = subscribeRequestRepository
                .findByMerchantTransactionCode(merchantTransactionCode).orElseThrow();

        final SubscribeResponseEntity subscribeResponseEntity =
                subscribeResponseRepository.findByMerchantTransactionCode(merchantTransactionCode).orElseThrow();

        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();

        if (!subscribeRequestEntity.getMerchantTransactionCode().equals(merchantTransactionCode)) {
            response.setMessage(ResponseMessages.PRE_CHECKOUT_VALIDATION_FAILED);
            response.setStatus(false);
            return response;
        }

        if (!subscribeRequestEntity.getApiKey().equals(authenticate.getApiKey())) {
            response.setMessage(ResponseMessages.SUBSCRIBE_FAILED);
            response.setStatus(false);
            return response;
        }

        final String bankTransactionCode = authenticate.getBankTransactionCode();
        if (!subscribeResponseEntity.getSolidBankTransactionCode().equals(bankTransactionCode)) {
            response.setMessage(ResponseMessages.PRE_CHECKOUT_VALIDATION_FAILED);
            response.setStatus(false);
            return response;
        }

        final PreCheckoutEntity preCheckoutEntity = new PreCheckoutEntity(authenticate.getAmount(), authenticate.getFailureRedirectURL(),
                                                                          authenticate.getSuccessRedirectURL(), authenticate.getCurrency(),
                                                                          ResponseMessages.PRE_CHECKOUT_VALIDATION_SUCCESS,
                                                                          authenticate.getBankTransactionCode(),
                                                                          authenticate.getMerchantTransactionCode());

        preCheckoutRepository.save(preCheckoutEntity);

        response.setStatus(true);
        response.setMessage(ResponseMessages.PRE_CHECKOUT_VALIDATION_SUCCESS);
        response.setSolidBankTransactionCode(bankTransactionCode);
        response.setPaymentUrl("/payment.html");

        return response;
    }


    private String validateSubscribe(final String merchantTransactionCode, final String apiKey, final String amount,
                                      final String currency) {

        if (merchantTransactionCode == null || apiKey == null || amount == null || currency == null) {
            log.debug("merchantTransactionCode {} apiKey {} amount {} currency {}", merchantTransactionCode, apiKey, amount, currency);
            return "";
        }

        if (apiKey.length() != 16 || !this.apiKey.equals(apiKey)) {
            log.debug("apiKey {} is not valid", apiKey);
            return "";
        }

        if (merchantTransactionCode.length() != 16) {
            log.debug("merchantTransactionCode {} is not valid. Length is {}", merchantTransactionCode, merchantTransactionCode.length());
            return "";
        }

        final long amountLong = Long.parseLong(amount);
        if (amountLong < 0 || amountLong > 1000000) {
            log.debug("Amount is less than 0 or higher than 1000000 {}", amountLong);
            return "";
        }

        if (!currencies.contains(currency)) {
            log.debug("currency {} is not valid", currency);
            return "";
        }

        if (subscribeRequestRepository.findByMerchantTransactionCode(merchantTransactionCode).isPresent()) {
            log.debug("merchantTransactionCode: {} already subscribed", merchantTransactionCode);
            return "";
        }

        return null;
    }
}
