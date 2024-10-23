package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.dto.SubscribeResponseDTO;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import com.solid.soft.solid_soft_bank.service.CardService;
import com.solid.soft.solid_soft_bank.service.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.InstanceAlreadyExistsException;

@Controller
@RequestMapping("/bank")
public class BankResource {

    private static final Logger log = LoggerFactory.getLogger(BankResource.class);

    @Value("${application.sample.card.sample-card-no}")
    private String sampleCardNo;
    @Value("${application.temporary.otp}")
    public String temporaryOtp;
    @Value("${application.payment.otp-url}")
    public String otpUrl;

    private final AuthenticateService authenticateService;
    private final SubscribeService subscribeService;
    private final CardService cardService;

    public BankResource(final AuthenticateService authenticateService, final SubscribeService subscribeService, final CardService cardService) {
        this.authenticateService = authenticateService;
        this.subscribeService = subscribeService;
        this.cardService = cardService;
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public ResponseEntity<SubscribeResponseDTO> subscribe(@RequestParam(name = "merchantTransactionCode") String merchantTransactionCode,
                                                          @RequestParam(name = "apiKey") String apiKey,
                                                          @RequestParam(name = "amount") Double amount,
                                                          @RequestParam(name = "currency") String currency) {
        log.debug("Post request to subscribe...");
        return ResponseEntity.ok(subscribeService.subscribe(merchantTransactionCode, apiKey, amount, currency));
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public AuthenticateResponseDTO authenticate(@RequestBody AuthenticateRequestDTO authenticateRequestDTO) throws InstanceAlreadyExistsException {
        log.debug("Post request to authenticate...");
        return authenticateService.authenticatePrePayment(authenticateRequestDTO);
    }

    @PostMapping("/authenticate/otp")
    @ResponseBody
    public AuthenticateResponseDTO authenticateAndRedirectOtpPage(@RequestBody AuthenticateRequestDTO requestDTO) throws InstanceAlreadyExistsException {
        final AuthenticateResponseDTO authenticateResponseDTO = authenticateService.authenticatePrePayment(requestDTO);
        if (!authenticateResponseDTO.isStatus()) {
            return authenticateResponseDTO;
        }

        final CardDTO card = cardService.findByCardNo(requestDTO.getCard().getCardNo());
        if (!card.getOtpRequired()){
            return authenticateService.createAuthenticateResponse(authenticateResponseDTO.getId(), false, "Card not supported OTP", authenticateResponseDTO.getBankTransactionCode(), null);
        }
        authenticateResponseDTO.setUrl(this.otpUrl + authenticateResponseDTO.getBankTransactionCode());
        return authenticateResponseDTO;
    }

    @GetMapping("/checkout")
    public String showPaymentForm(@RequestParam String bankTransactionCode, Model model) {

        log.debug("Get request to checkout...");

        final PaymentTransactionEntryDTO authenticateEntry = authenticateService.findByBankTransactionCode(bankTransactionCode);
        final PaymentTransactionEntryDTO subsribeEntry = subscribeService.findSubscribeEntryByMerchantTransactionCode(
                authenticateEntry.getPaymentTransactionDto().getMerchantTransactionCode());

        if (!subsribeEntry.isStatus() || !authenticateEntry.isStatus()) {
            return "redirect:" + authenticateEntry.getFailedRedirectURL();
        }

        final CardDTO card = new CardDTO();
        card.setAmount(authenticateEntry.getAmount());
        card.setCurrency(authenticateEntry.getCurrency());
       // addSampleCardOnForm(card);

        model.addAttribute("card", card);
        model.addAttribute("bankTransactionCode", bankTransactionCode);

        log.debug("CardDTO: {}", card);
        return "payment";
    }


    @GetMapping("/subscribe")
    @ResponseBody
    public ResponseEntity<PaymentTransactionEntryDTO> findSubscribeByMerchantTransactionCode(@RequestParam(name = "merchantTransactionCode") String merchantTransactionCode) {

        final PaymentTransactionEntryDTO responseDTO = subscribeService.findSubscribeEntryByMerchantTransactionCode(merchantTransactionCode);
        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseDTO);
    }

    private void addSampleCardOnForm(final CardDTO card) {
        final CardDTO cardEntity = cardService.findByCardNo(sampleCardNo);
        if (cardEntity != null) {
            card.setCardNo(sampleCardNo);
            card.setName(cardEntity.getName());
            card.setSurname(cardEntity.getSurname());
            card.setCvc(cardEntity.getCvc());
            card.setExpiredDate(cardEntity.getExpiredDate());
        }
    }

}
