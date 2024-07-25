package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.service.AuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateResource {

    private final AuthenticateService authenticateService;

    public AuthenticateResource(final AuthenticateService authenticateService) {this.authenticateService = authenticateService;}

    @PostMapping
    public AuthenticateResponseDTO authenticate(@RequestBody AuthenticateRequestDTO authenticateRequestDTO) {
        return authenticateService.authenticatePrePayment(authenticateRequestDTO);
    }
}
