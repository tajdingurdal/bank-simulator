package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.mapper.MerchantMapper;
import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.repository.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantService {

    private static final Logger             log = LoggerFactory.getLogger(MerchantService.class);
    private final        MerchantRepository repository;
    private final        MerchantMapper     mapper;

    public MerchantService(final MerchantRepository repository, final MerchantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public MerchantDTO findByApikey(String apikey) {
        final Optional<MerchantEntity> merchantEntity = repository.findByApiKey(apikey);
        return merchantEntity.map(mapper::toDto).orElse(null);
    }

    public MerchantDTO create(String name, String webSite) throws InstanceAlreadyExistsException {
        final Optional<MerchantEntity> merchantEntityByName    = repository.findByName(name);
        final Optional<MerchantEntity> merchantEntityByWebsite = repository.findByWebSite(webSite);

        if (merchantEntityByName.isPresent() || merchantEntityByWebsite.isPresent()) {
            throw new InstanceAlreadyExistsException("Merchant already created by this name and website");
        }

        String apiKey = String.format(UUID.randomUUID() + "%s%s%s", name.toLowerCase().replaceAll(" ", ""), webSite, ZonedDateTime.now().toInstant().toEpochMilli());

        final MerchantEntity savedMerchantEntity = repository.save(new MerchantEntity(name, webSite, apiKey));
        final MerchantDTO dto = mapper.toDto(savedMerchantEntity);
        log.debug("Merchant created! {}", savedMerchantEntity);
        return dto;
    }

    public MerchantDTO findMerchantById(Long id) {
        final Optional<MerchantEntity> merchantEntity = repository.findById(id);
        log.debug("Found Merchant by id {}", merchantEntity);
        return merchantEntity.map(mapper::toDto).orElse(null);
    }
}
