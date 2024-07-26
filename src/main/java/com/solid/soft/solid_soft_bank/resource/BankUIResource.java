package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.repository.CardRepository;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import com.solid.soft.solid_soft_bank.service.SubscribeService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/bank/ui")
public class BankUIResource {

    private static final Logger log = LoggerFactory.getLogger(BankUIResource.class);
    private static final String OTP_CODE = "12345";

    private final AuthenticateService authenticateService;
    private final SubscribeService subscribeService;
    private final CardRepository cardRepository;

    public BankUIResource(final AuthenticateService authenticateService,
                          final SubscribeService subscribeService, final CardRepository cardRepository) {
        this.authenticateService = authenticateService;
        this.subscribeService = subscribeService;
        this.cardRepository = cardRepository;
    }

    @RequestMapping(value = "/payment-page", method = RequestMethod.GET)
    public String showPaymentForm(@RequestParam String bankTransactionCode, Model model) {

        final PaymentTransactionEntryDTO authenticateEntry = authenticateService.findByBankTransactionCode(bankTransactionCode);
        final PaymentTransactionEntryDTO subsribeEntry = subscribeService.findSubscribeEntryByMerchantTransactionCode(
                authenticateEntry.getPaymentTransactionDto().getMerchantTransactionCode());

        if (!subsribeEntry.isStatus() || !authenticateEntry.isStatus()) {
            return "redirect:" + authenticateEntry.getFailedRedirectURL();
        }

        final CardDTO card = new CardDTO();
        card.setAmount(authenticateEntry.getAmount());
        card.setCurrency(authenticateEntry.getCurrency());
        addSampleCardOnForm(card);

        model.addAttribute("card", card);
        model.addAttribute("bankTransactionCode", bankTransactionCode);

        log.debug("CardDTO: {}", card);
        return "payment";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(@RequestParam String bankTransactionCode, @ModelAttribute("card") CardDTO card, Model model) {

        log.debug("CardDTO: {}", card);
        final PaymentTransactionEntryDTO authenticateEntity = authenticateService.findByBankTransactionCode(bankTransactionCode);

        try {
            final String authenticateResult = authenticateService.authenticatePaymentProcess(bankTransactionCode, card);

            if (authenticateResult != null) {
                return "redirect:" + authenticateEntity.getFailedRedirectURL();
            }

            model.addAttribute("otpCode", OTP_CODE);
            model.addAttribute("bankTransactionCode", bankTransactionCode);
            return "otp";
        } catch (Throwable t) {
            t.printStackTrace();
            return "redirect:" + authenticateEntity.getFailedRedirectURL();
        }

    }

    @GetMapping("/validate-otp")
    public String otp(@RequestParam String otpCode, @RequestParam String bankTransactionCode, Model model) {
        log.debug("otpCode : {}", otpCode);
        final PaymentTransactionEntryDTO authenticateEntity = authenticateService.findByBankTransactionCode(bankTransactionCode);

        if (authenticateEntity == null) {
            throw new EntityNotFoundException(String.format("Payment Transaction not found by Bank Transaction Code %s", bankTransactionCode));
        }

        if (!otpCode.equals(OTP_CODE)) {
            return "redirect:" + authenticateEntity.getFailedRedirectURL();
        }

        return "redirect:" + authenticateEntity.getSuccessRedirectURL();
    }

    private void addSampleCardOnForm(final CardDTO card) {
        final String sampleCardNo = "1234567890";
        final Optional<CardEntity> cardEntity = cardRepository.findByCardNo(sampleCardNo);
        if(cardEntity.isPresent()){
            card.setCardNo(sampleCardNo);
            card.setName(cardEntity.get().getName());
            card.setSurname(cardEntity.get().getSurname());
            card.setCvc(cardEntity.get().getCvc());
            card.setExpiredDate(cardEntity.get().getExpiredDate());
        }
    }

}
