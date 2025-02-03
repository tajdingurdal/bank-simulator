package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.service.dto.CaptureRequest;
import org.springframework.stereotype.Service;

@Service
public class CaptureService {

    private final CardService cardService;
    private final CallBackService callBackService;

    public CaptureService(CardService cardService, CallBackService callBackService) {
        this.cardService = cardService;
        this.callBackService = callBackService;
    }

    public boolean capture(CaptureRequest request) {
        cardService.decreaseBalance(request.getCardNo(), request.getAmount());
        return true;
    }

}
