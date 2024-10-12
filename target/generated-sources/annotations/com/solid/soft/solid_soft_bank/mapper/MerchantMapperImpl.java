package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T21:51:32+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class MerchantMapperImpl implements MerchantMapper {

    @Override
    public MerchantDTO toDto(MerchantEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MerchantDTO merchantDTO = new MerchantDTO();

        merchantDTO.setName( entity.getName() );
        merchantDTO.setWebSite( entity.getWebSite() );
        merchantDTO.setApiKey( entity.getApiKey() );
        merchantDTO.setId( entity.getId() );

        return merchantDTO;
    }
}
