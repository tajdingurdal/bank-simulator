package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationRequest;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationResponse;
import com.solid.soft.solid_soft_bank.resource.dto.PaymentInitializationRequest;
import com.solid.soft.solid_soft_bank.resource.dto.PaymentInitializationResponse;
import com.solid.soft.solid_soft_bank.service.CardService;
import com.solid.soft.solid_soft_bank.service.PaymentAuthenticationService;
import com.solid.soft.solid_soft_bank.service.PaymentInitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private final PaymentInitializationService paymentInitService;
    private final PaymentAuthenticationService authService;
    private final CardService cardService;


    public PaymentResource(PaymentInitializationService paymentInitService, PaymentAuthenticationService authService, CardService cardService) {
        this.paymentInitService = paymentInitService;
        this.authService = authService;
        this.cardService = cardService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<PaymentInitializationResponse> initializePayment(@RequestBody PaymentInitializationRequest request) {
        log.debug("Initializing payment for merchant transaction: {}", request.getMerchantTransactionCode());
        return ResponseEntity.ok(paymentInitService.initializePayment(request));
    }

    @PostMapping("/authenticate-and-prepare-otp")
    public AuthenticationResponse authenticatePaymentAndPrepareOtp(@RequestBody AuthenticationRequest dto) throws InstanceAlreadyExistsException {

        // Step 1: Authenticate the payment
        dto.setExternalProcess(true);
        final AuthenticationResponse authResult = authService.authenticatePrePayment(dto);
        if (!authResult.isStatus()) {
            return authResult ;
        }

        // Step 2: Check OTP eligibility
        final CardDTO card = cardService.findByCardNo(dto.getCard().getCardNo());
        if (authResult.isOtpRequired() && !card.getOtpRequired()) {
            return authService.createAuthenticateResponse(
                    authResult.getId(),
                    false,
                    "Card not supported OTP",
                    authResult.getBankTransactionCode(),
                    null,
                    card.getOtpRequired());

        }

        return authResult ;
    }

    @GetMapping("/transaction/{merchantTransactionCode}")
    public ResponseEntity<PaymentTransactionEntryDTO> findSubscribeByMerchantTransactionCode(@PathVariable String merchantTransactionCode) {
        log.debug("Fetching transaction details for merchant transaction: {}", merchantTransactionCode);
        final PaymentTransactionEntryDTO response = paymentInitService.findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode);
        return Optional.of(response)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
