package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.UserCard;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bank/ui")
public class BankUIResource {

    private static final Logger log = LoggerFactory.getLogger(BankUIResource.class);
    private final BankService bankService;

    public BankUIResource(final BankService bankService) {this.bankService = bankService;}

    @PostMapping("/authenticate")
    public String preCheckout(@RequestBody AuthenticateRequestDTO authenticateRequestDTO, Model model) {

        AuthenticateResponseDTO response = bankService.authenticate(authenticateRequestDTO);

        final UserCard userCard = new UserCard();
        userCard.setAmount(authenticateRequestDTO.getAmount());
        userCard.setCurrency(authenticateRequestDTO.getCurrency());
        model.addAttribute("user", userCard);
        model.addAttribute("response", response);

        if (!response.isStatus()) {
            return "failed_page";
        }

        return "payment.html";
    }

    @PostMapping("/otp")
    public String otp(@ModelAttribute("user") UserCard user) {
        log.debug("User : {}", user);
        return null;
    }
}
