package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import com.solid.soft.solid_soft_bank.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.InstanceAlreadyExistsException;

@Controller
@RequestMapping("/bank/ui")
public class BankUIResource {

    private static final Logger log = LoggerFactory.getLogger(BankUIResource.class);
    @Value("${temporary.otp}")
    public String temporaryOtp;

    private final AuthenticateService authenticateService;
    private final CardService cardService;

    public BankUIResource(final AuthenticateService authenticateService, final CardService cardService) {
        this.authenticateService = authenticateService;
        this.cardService = cardService;
    }

    @RequestMapping(value = "/authenticate/otp", method = RequestMethod.POST)
    public String authenticateAndRedirectOtpPage(@RequestBody AuthenticateRequestDTO requestDTO, Model model) throws InstanceAlreadyExistsException {
        final AuthenticateResponseDTO authenticateResponseDTO = authenticateService.authenticatePrePayment(requestDTO);
        if (!authenticateResponseDTO.isStatus()) {
            return authenticateResponseDTO.getMessage();
        }

        final CardDTO card = cardService.findByCardNo(requestDTO.getCard().getCardNo());
        if (!card.getOtpRequired()){
            return "Card not supported OTP";
        }

        model.addAttribute("otpCode", temporaryOtp);
        model.addAttribute("bankTransactionCode", authenticateResponseDTO.getBankTransactionCode());
        model.addAttribute("cardNo", requestDTO.getCard().getCardNo());
        return "otp";
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

            model.addAttribute("otpCode", temporaryOtp);
            model.addAttribute("bankTransactionCode", bankTransactionCode);
            model.addAttribute("cardNo", card.getCardNo());
            return "otp";
        } catch (Throwable t) {
            t.printStackTrace();
            return "redirect:" + authenticateEntity.getFailedRedirectURL();
        }

    }

    @GetMapping("/validate-otp")
    public String otp(@RequestParam String otpCode, @RequestParam String bankTransactionCode, @RequestParam String cardNo,  Model model) {
        final PaymentTransactionEntryDTO authenticateEntity = authenticateService.findByBankTransactionCode(bankTransactionCode);
        if (authenticateEntity == null) {
            throw new EntityNotFoundException(String.format("Payment Transaction not found by Bank Transaction Code %s", bankTransactionCode));
        }

        if (!otpCode.equals(temporaryOtp)) {
            log.debug("OTP Validation failed : {}", otpCode);
            return "redirect:" + authenticateEntity.getFailedRedirectURL();
        }
        cardService.updateBalanceAndLastTransactionTime(cardNo, authenticateEntity.getAmount());
        log.debug("OTP Validation success : {}", otpCode);
        return "redirect:" + authenticateEntity.getSuccessRedirectURL();
    }


}
