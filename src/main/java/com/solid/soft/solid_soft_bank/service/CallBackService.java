package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.enums.CallbackStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CallBackService {

    private final RestTemplate restTemplate;
    private final MerchantService merchantService;
    private final PaymentTransactionService paymentTransactionService;

    public CallBackService(RestTemplate restTemplate, MerchantService merchantService, PaymentTransactionService paymentTransactionService) {
        this.restTemplate = restTemplate;
        this.merchantService = merchantService;
        this.paymentTransactionService = paymentTransactionService;
    }

    public void sendCallBack(final MerchantDTO merchant, final CallbackStatus status, final String merchantTransactionCode) {
        String serverApiUrl = merchant.getServerApiUrl();
        String callbackEndpoint = merchant.getCallbackEndpoint();
        String format = String.format("%s/%s?status=%s&merchantTransactionCode=%s", serverApiUrl, callbackEndpoint, status, merchantTransactionCode);
     //   restTemplate.put(format, null);
    }
}
