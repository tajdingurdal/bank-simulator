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
import org.springframework.beans.factory.annotation.Value;
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
    private final PaymentInitializationService paymentInitializationService;
    private final CardService cardService;

    @Value("${application.payment.otp-url}")
    public String otpUrl;

    public PaymentResource(PaymentInitializationService paymentInitService, PaymentAuthenticationService authService, PaymentInitializationService paymentInitializationService, CardService cardService) {
        this.paymentInitService = paymentInitService;
        this.authService = authService;
        this.paymentInitializationService = paymentInitializationService;
        this.cardService = cardService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<PaymentInitializationResponse> initializePayment(@RequestBody PaymentInitializationRequest request) {
        log.debug("Initializing payment for merchant transaction: {}", request.getMerchantTransactionCode());
        return ResponseEntity.ok(paymentInitService.initializePayment(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticatePayment(@RequestBody AuthenticationRequest request) throws InstanceAlreadyExistsException {
        log.debug("Authenticating payment...");
        return ResponseEntity.ok(authService.authenticatePrePayment(request));
    }

    @PostMapping("/authenticate-and-prepare-otp")
    public AuthenticationResponse authenticatePaymentAndPrepareOtp(@RequestBody AuthenticationRequest dto) throws InstanceAlreadyExistsException {

        // Step 1: Authenticate the payment
        final AuthenticationResponse authResult = authService.authenticatePrePayment(dto);
        if (!authResult .isStatus()) {
            return authResult ;
        }

        // Step 2: Check OTP eligibility
        final CardDTO card = cardService.findByCardNo(dto.getCard().getCardNo());
        if (!card.getOtpRequired()) {
            return authService.createAuthenticateResponse(
                    authResult.getId(),
                    false,
                    "Card not supported OTP",
                    authResult.getBankTransactionCode(),
                    null);

        }

        // Step 3: Set OTP page URL
        authResult.setUrl(this.otpUrl + authResult .getBankTransactionCode() + "&cardNo=" + card.getCardNo());

        return authResult ;
    }

    @GetMapping("/transaction/{merchantTransactionCode}")
    public ResponseEntity<PaymentTransactionEntryDTO> findSubscribeByMerchantTransactionCode(@PathVariable String merchantTransactionCode) {
        log.debug("Fetching transaction details for merchant transaction: {}", merchantTransactionCode);
        final PaymentTransactionEntryDTO response = paymentInitializationService.findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode);
        return Optional.of(response)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
