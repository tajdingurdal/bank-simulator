package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.service.MerchantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceAlreadyExistsException;

@RestController
@RequestMapping("/merchant")
public class MerchantResource {

    private final MerchantService merchantService;

    public MerchantResource(final MerchantService merchantService) {this.merchantService = merchantService;}

    @PostMapping
    public MerchantDTO create(@RequestParam String name, @RequestParam String webSite) throws InstanceAlreadyExistsException {
        return merchantService.create(name, webSite);
    }

    @GetMapping("/{id}")
    public MerchantDTO findMerchantById(@PathVariable("id") Long id) {
        return merchantService.findMerchantById(id);
    }
}
