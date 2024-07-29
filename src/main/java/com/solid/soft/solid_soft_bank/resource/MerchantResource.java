package com.solid.soft.solid_soft_bank.resource;

import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.service.MerchantService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MerchantDTO> create(@RequestParam String name, @RequestParam String webSite) throws InstanceAlreadyExistsException {
        return ResponseEntity.ok(merchantService.create(name, webSite));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> findMerchantById(@PathVariable("id") Long id) {
        final MerchantDTO merchantDTO = merchantService.findMerchantById(id);
        if (merchantDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchantDTO);
    }

    @GetMapping
    public ResponseEntity<MerchantDTO> findMerchantByNameAndWebsite(@RequestParam String name, @RequestParam String webSite) {
        final MerchantDTO merchantDTO = merchantService.findMerchantByNameAndWebsite(name, webSite);
        if (merchantDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchantDTO);
    }

    @GetMapping("/api-key/{apiKey}")
    public ResponseEntity<MerchantDTO> findMerchantByApiKey(@PathVariable("apiKey") String apiKey) {
        final MerchantDTO merchantDTO = merchantService.findByApikey(apiKey);
        if (merchantDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchantDTO);
    }
}
