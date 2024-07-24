package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.AuthenticateEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeRequestEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.repository.AuthenticateRepository;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    private final AuthenticateRepository repository;

    private final AuthenticateRepository authenticateRepository;
    private final SubscribeService       subscribeService;

    public AuthenticateService(final AuthenticateRepository repository, final AuthenticateRepository authenticateRepository,
                               final SubscribeService subscribeService) {
        this.repository = repository;
        this.authenticateRepository = authenticateRepository;
        this.subscribeService = subscribeService;
    }

    public AuthenticateEntity findByBankTransactionCode(String bankTransactionCode) {
        return repository.findBySolidBankTransactionCode(bankTransactionCode).orElseThrow();
    }

    public AuthenticateResponseDTO authenticate(final AuthenticateRequestDTO authenticate) {

        final String merchantTransactionCode = authenticate.getMerchantTransactionCode();
        final SubscribeRequestEntity subscribeRequestEntity = subscribeService.findSubscribeRequestByMerchantTransactionCode(
                merchantTransactionCode);

        final SubscribeResponseEntity subscribeResponseEntity = subscribeService.findSubscribeResponseByMerchantTransactionCode(
                merchantTransactionCode);

        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();

        if (!subscribeRequestEntity.getMerchantTransactionCode().equals(merchantTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            return response;
        }

        if (!subscribeRequestEntity.getApiKey().equals(authenticate.getApiKey())) {
            response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
            response.setStatus(false);
            return response;
        }

        final String bankTransactionCode = authenticate.getBankTransactionCode();
        if (!subscribeResponseEntity.getSolidBankTransactionCode().equals(bankTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            return response;
        }

        final AuthenticateEntity authenticateEntity = new AuthenticateEntity(authenticate.getAmount(), authenticate.getFailureRedirectURL(),
                                                                             authenticate.getSuccessRedirectURL(),
                                                                             authenticate.getCurrency(),
                                                                             ResponseMessages.AUTHENTICATE_SUCCESS,
                                                                             authenticate.getBankTransactionCode(),
                                                                             authenticate.getMerchantTransactionCode(),
                                                                             true);

        authenticateRepository.save(authenticateEntity);

        response.setStatus(true);
        response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
        response.setSolidBankTransactionCode(bankTransactionCode);
        response.setPaymentUrl("http://localhost:8080/bank/ui/payment-page?bankTransactionCode=" + bankTransactionCode);

        return response;
    }
}
