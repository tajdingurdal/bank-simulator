package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.mapper.MerchantMapper;
import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
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

    public MerchantDTO create(final MerchantDTO dto) throws InstanceAlreadyExistsException {
        if (repository.existsByNameAndWebSite(dto.getName(), dto.getWebSite())) {
            throw new InstanceAlreadyExistsException("Merchant already created by this name and website");
        }

        String apiKey = String.format(UUID.randomUUID() + "%s%s%s", dto.getName().toLowerCase().replaceAll(" ", ""), dto.getWebSite(), ZonedDateTime.now().toInstant().toEpochMilli());
        MerchantEntity entity = mapper.toEntity(dto);
        entity.setApiKey(apiKey);
        final MerchantEntity savedMerchantEntity = repository.save(entity);
        log.debug("Merchant created! {}", savedMerchantEntity);
        return mapper.toDto(savedMerchantEntity);
    }

    public MerchantDTO findMerchantById(Long id) {
        final Optional<MerchantEntity> merchantEntity = repository.findById(id);
        log.debug("Found Merchant by id {}", merchantEntity);
        return merchantEntity.map(mapper::toDto).orElse(null);
    }

    public MerchantDTO findMerchantByNameAndWebsite(final String name, final String webSite) {
        final Optional<MerchantEntity> merchantEntity = repository.findByNameAndWebSite(name, webSite);
        return merchantEntity.map(mapper::toDto).orElse(null);
    }
}
