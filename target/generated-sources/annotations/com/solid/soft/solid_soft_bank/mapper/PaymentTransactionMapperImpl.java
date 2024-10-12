package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T21:51:32+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class PaymentTransactionMapperImpl implements PaymentTransactionMapper {

    @Override
    public PaymentTransactionEntity toEntity(PaymentTransactionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();

        paymentTransactionEntity.setId( dto.getId() );
        paymentTransactionEntity.setBankTransactionCode( dto.getBankTransactionCode() );
        paymentTransactionEntity.setMerchantTransactionCode( dto.getMerchantTransactionCode() );
        paymentTransactionEntity.setMerchandId( dto.getMerchandId() );
        paymentTransactionEntity.setMerchantEntity( merchantDTOToMerchantEntity( dto.getMerchantEntity() ) );

        return paymentTransactionEntity;
    }

    @Override
    public PaymentTransactionDTO toDto(PaymentTransactionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();

        paymentTransactionDTO.setId( entity.getId() );
        paymentTransactionDTO.setBankTransactionCode( entity.getBankTransactionCode() );
        paymentTransactionDTO.setMerchantTransactionCode( entity.getMerchantTransactionCode() );
        paymentTransactionDTO.setMerchandId( entity.getMerchandId() );
        paymentTransactionDTO.setMerchantEntity( merchantEntityToMerchantDTO( entity.getMerchantEntity() ) );

        return paymentTransactionDTO;
    }

    protected MerchantEntity merchantDTOToMerchantEntity(MerchantDTO merchantDTO) {
        if ( merchantDTO == null ) {
            return null;
        }

        MerchantEntity merchantEntity = new MerchantEntity();

        merchantEntity.setId( merchantDTO.getId() );
        merchantEntity.setName( merchantDTO.getName() );
        merchantEntity.setWebSite( merchantDTO.getWebSite() );
        merchantEntity.setApiKey( merchantDTO.getApiKey() );

        return merchantEntity;
    }

    protected MerchantDTO merchantEntityToMerchantDTO(MerchantEntity merchantEntity) {
        if ( merchantEntity == null ) {
            return null;
        }

        MerchantDTO merchantDTO = new MerchantDTO();

        merchantDTO.setName( merchantEntity.getName() );
        merchantDTO.setWebSite( merchantEntity.getWebSite() );
        merchantDTO.setApiKey( merchantEntity.getApiKey() );
        merchantDTO.setId( merchantEntity.getId() );

        return merchantDTO;
    }
}
