package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.SubscribeResponseDTO;
import com.solid.soft.solid_soft_bank.service.SubscribeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class SubscribeResource {

    private final SubscribeService bankService;

    public SubscribeResource(final SubscribeService bankService) {this.bankService = bankService;}

    @PostMapping("/subscribe")
    public SubscribeResponseDTO subscribe(@RequestParam(name = "merchantTransactionCode") String merchantTransactionCode,
                                          @RequestParam(name = "apiKey") String apiKey,
                                          @RequestParam(name = "amount") String amount,
                                          @RequestParam(name = "currency") String currency) {

        return bankService.subscribe(merchantTransactionCode, apiKey, amount, currency);
    }
}
