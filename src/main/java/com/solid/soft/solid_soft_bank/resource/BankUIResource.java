package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.AuthenticateEntity;
import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.UserCard;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import com.solid.soft.solid_soft_bank.service.MerchantService;
import com.solid.soft.solid_soft_bank.service.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bank/ui")
public class BankUIResource {

    private static final Logger              log = LoggerFactory.getLogger(BankUIResource.class);
    private final        AuthenticateService authenticateService;
    private final        SubscribeService    subscribeService;
    private final        MerchantService     merchantService;

    public BankUIResource(final AuthenticateService authenticateService,
                          final SubscribeService subscribeService, final MerchantService merchantService) {
        this.authenticateService = authenticateService;
        this.subscribeService = subscribeService;
        this.merchantService = merchantService;
    }

    @RequestMapping(value = "/payment-page", method = RequestMethod.GET)
    public String showPaymentForm(@RequestParam String bankTransactionCode, Model model) {

        final AuthenticateEntity      authenticateEntity      = authenticateService.findByBankTransactionCode(bankTransactionCode);
        final SubscribeResponseEntity subscribeResponseEntity = subscribeService.findSubscribeResponseByMerchantTransactionCode(authenticateEntity.getMerchantTransactionCode());

        if (!subscribeResponseEntity.isSubscribe() || !authenticateEntity.isAuthenticate()) {
            return "null";
        }
        final MerchantEntity merchantEntity = merchantService.findByApikey(authenticateEntity.getApiKey());

        final UserCard user = new UserCard();
        user.setAmount(authenticateEntity.getAmount());
        user.setCurrency(authenticateEntity.getCurrency());
        user.setMerchantName(merchantEntity.getName());
        model.addAttribute("user", user);
        model.addAttribute("bankTransactionCode", bankTransactionCode);
        log.info("UserCard: {}", user);
        return "payment";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String bankTransactionCode, @ModelAttribute("user") UserCard user) {
        log.info("User: {}", user);
        // validate user card in DB
        // adamın kartını valide et, sendekiyle aynı mı
        // bankTransactionCode kullanarak DBden çekilecek olan amountu bul, hesabında para var mı


        //başarısız ise
        // Authenticate entitysinden failureCallbackUrl'i bul
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
