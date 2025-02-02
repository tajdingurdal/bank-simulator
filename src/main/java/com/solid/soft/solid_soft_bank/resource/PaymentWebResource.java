package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.exception.PaymentAuthenticationException;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.service.CallBackService;
import com.solid.soft.solid_soft_bank.service.CaptureService;
import com.solid.soft.solid_soft_bank.service.PaymentAuthenticationService;
import com.solid.soft.solid_soft_bank.service.PaymentWebService;
import com.solid.soft.solid_soft_bank.service.dto.CaptureRequest;
import com.solid.soft.solid_soft_bank.service.dto.CheckoutPageData;
import com.solid.soft.solid_soft_bank.service.dto.OtpValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.solid.soft.solid_soft_bank.model.enums.CallbackStatus.FAILURE;

@Controller
@RequestMapping("/payment/ui")
public class PaymentWebResource {

    private final CaptureService captureService;
    Logger log = LoggerFactory.getLogger(PaymentWebResource.class);

    private final PaymentWebService paymentWebService;
    private final PaymentAuthenticationService authService;
    private final CallBackService callBackService;

    @Value("${application.temporary.otp}")
    public String temporaryOtp;

    public PaymentWebResource(PaymentAuthenticationService authService, CallBackService callBackService, PaymentWebService paymentWebService, CaptureService captureService) {
        this.authService = authService;
        this.callBackService = callBackService;
        this.paymentWebService = paymentWebService;
        this.captureService = captureService;
    }

    @GetMapping("/checkout")
    public String showPaymentForm(@RequestParam String bankTransactionCode, Model model) {

        log.debug("Initiating checkout for transaction: {}", bankTransactionCode);
        CheckoutPageData checkoutPageData = paymentWebService.preparePaymentForm(bankTransactionCode);
        if (!checkoutPageData.isStatus()) {
            return "redirect:" + checkoutPageData.getMerchant().getFailedRedirectURL();
        }
        model.addAttribute("card", checkoutPageData.getCard());
        model.addAttribute("bankTransactionCode", bankTransactionCode);
        return "payment";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String pay(@RequestParam String bankTransactionCode,
                      @RequestParam String cardNo,
                      @ModelAttribute("card") CardDTO card,
                      Model model) {

        log.debug("Processing payment authentication for card: {}", cardNo);

        final PaymentTransactionEntryDTO authenticateEntry = authService.findByBankTransactionCode(bankTransactionCode);
        MerchantDTO merchant = authenticateEntry.getPaymentTransactionDto().getMerchantEntity();
        try {
            if (Objects.nonNull(card.getName())) {
                final String authenticateResult = authService.authenticatePaymentProcess(bankTransactionCode, card);
                if (authenticateResult != null) {
                    return "redirect:" + merchant.getFailedRedirectURL() + "?message=" + authenticateResult.toLowerCase().replace(" ", "-");
                }
            }
            model.addAttribute("otpCode", temporaryOtp);
            model.addAttribute("bankTransactionCode", bankTransactionCode);
            model.addAttribute("cardNo", card.getCardNo() == null ? cardNo : card.getCardNo());
            return "otp";
        } catch (PaymentAuthenticationException e) {
            log.error("Authentication failed", e);
            return "redirect:" + merchant.getFailedRedirectURL() + "?message=" + authenticateEntry.getResultMessage().toLowerCase().replace(" ", "-");
        }

    }

    @GetMapping("/validate-otp")
    public String otp(@RequestParam String otpCode,
                      @RequestParam String bankTransactionCode,
                      @RequestParam String cardNo,
                      Model model) {

        log.debug("Validating OTP for transaction: {}", bankTransactionCode);
        OtpValidationResult validationResult = authService.validateOtp(otpCode, bankTransactionCode, cardNo);
        MerchantDTO merchant = validationResult.getMerchant();

        if (validationResult.isValid()) {
            return "redirect:" + merchant.getSuccessRedirectURL();
        }
        return "redirect:" + merchant.getFailedRedirectURL() + "?message=" + "otp-not-correct";
    }

}
