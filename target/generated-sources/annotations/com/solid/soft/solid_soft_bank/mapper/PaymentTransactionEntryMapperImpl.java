package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T21:51:32+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class PaymentTransactionEntryMapperImpl implements PaymentTransactionEntryMapper {

    @Override
    public PaymentTransactionEntryEntity toEntity(PaymentTransactionEntryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PaymentTransactionEntryEntity paymentTransactionEntryEntity = new PaymentTransactionEntryEntity();

        paymentTransactionEntryEntity.setId( dto.getId() );
        paymentTransactionEntryEntity.setTransactionType( dto.getTransactionType() );
        paymentTransactionEntryEntity.setStatus( dto.isStatus() );
        paymentTransactionEntryEntity.setResultMessage( dto.getResultMessage() );
        paymentTransactionEntryEntity.setSuccessRedirectURL( dto.getSuccessRedirectURL() );
        paymentTransactionEntryEntity.setFailedRedirectURL( dto.getFailedRedirectURL() );
        paymentTransactionEntryEntity.setCurrency( dto.getCurrency() );
        paymentTransactionEntryEntity.setAmount( dto.getAmount() );
        paymentTransactionEntryEntity.setCreateDate( dto.getCreateDate() );
        paymentTransactionEntryEntity.setPaymentTransactionId( dto.getPaymentTransactionId() );

        return paymentTransactionEntryEntity;
    }

    @Override
    public PaymentTransactionEntryDTO toDto(PaymentTransactionEntryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentTransactionEntryDTO paymentTransactionEntryDTO = new PaymentTransactionEntryDTO();

        paymentTransactionEntryDTO.setId( entity.getId() );
        paymentTransactionEntryDTO.setTransactionType( entity.getTransactionType() );
        paymentTransactionEntryDTO.setStatus( entity.isStatus() );
        paymentTransactionEntryDTO.setResultMessage( entity.getResultMessage() );
        paymentTransactionEntryDTO.setSuccessRedirectURL( entity.getSuccessRedirectURL() );
        paymentTransactionEntryDTO.setFailedRedirectURL( entity.getFailedRedirectURL() );
        paymentTransactionEntryDTO.setAmount( entity.getAmount() );
        paymentTransactionEntryDTO.setCurrency( entity.getCurrency() );
        paymentTransactionEntryDTO.setCreateDate( entity.getCreateDate() );
        paymentTransactionEntryDTO.setPaymentTransactionId( entity.getPaymentTransactionId() );

        return paymentTransactionEntryDTO;
    }
}
