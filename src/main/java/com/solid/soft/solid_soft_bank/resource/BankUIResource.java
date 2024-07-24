package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.AuthenticateEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.UserCard;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import com.solid.soft.solid_soft_bank.service.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bank/ui")
public class BankUIResource {

    private static final Logger              log = LoggerFactory.getLogger(BankUIResource.class);
    private final        AuthenticateService authenticateService;
    private final        SubscribeService    subscribeService;

    public BankUIResource(final AuthenticateService authenticateService,
                          final SubscribeService subscribeService) {
        this.authenticateService = authenticateService;
        this.subscribeService = subscribeService;
    }

    @PostMapping("/authenticate")
    public AuthenticateResponseDTO authenticate(@RequestBody AuthenticateRequestDTO authenticateRequestDTO, Model model) {

        AuthenticateResponseDTO response = authenticateService.authenticate(authenticateRequestDTO);

        final UserCard userCard = new UserCard();
        userCard.setAmount(authenticateRequestDTO.getAmount());
        userCard.setCurrency(authenticateRequestDTO.getCurrency());

        return response;
    }

    @RequestMapping(value = "/payment-page", method = RequestMethod.GET)
    public String showPaymentForm(@RequestParam String bankTransactionCode, Model model) {

        final AuthenticateEntity      authenticateEntity      = authenticateService.findByBankTransactionCode(bankTransactionCode);
        final SubscribeResponseEntity subscribeResponseEntity = subscribeService.findSubscribeResponseByMerchantTransactionCode(authenticateEntity.getMerchantTransactionCode());
        if (!subscribeResponseEntity.isSubscribe() || !authenticateEntity.isAuthenticate()) {
            return "null";
        }

        // TODO: VALIDATE TRANSCTION CODE IS VALID AND ALREADY AUTHORIZED
        model.addAttribute("user", new UserCard());
        model.addAttribute("bankTransactionCode", bankTransactionCode);

        // payment.html'in içinde bu filedları bas ki adam ne ödeyeceğini bilsin
        model.addAttribute("amount", amount);
        model.addAttribute("currency", currencyCode);
        model.addAttribute("merchant", merchantName);

        /*
        Bi tane merchant tablosu oluştur, id, name, website, apiKey olsun
         */
        return "payment";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String bankTransactionCode, @ModelAttribute("user") UserCard user) {
        // validate user card in DB
        // adamın kartını valide et, sendekiyle aynı mı
        // bankTransactionCode kullanarak DBden çekilecek olan amountu bul, hesabında para var mı


        //başarısız ise
        // PreCheckout entitysinden failureCallbackUrl'i bul
        // return "redirect:" + failureCallbackUrl

        // başarılı ise
        return "otp";
    }

    @PostMapping("/validate-otp")
    public String otp(@RequestParam String otpCode) {
        log.debug("User : {}", otpCode);

        // otp başarılı ise
        // PreCheckout entitysinden successCallbackUrl oku
        // return "redirect:" + successCallbackUrl

        //başarısız ise
        // PreCheckout entitysinden failureCallbackUrl'i bul
        // return "redirect:" + failureCallbackUrl
        return null;
    }
}
