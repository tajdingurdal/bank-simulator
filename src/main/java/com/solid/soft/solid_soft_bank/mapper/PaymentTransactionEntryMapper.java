package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentTransactionEntryMapper {

    PaymentTransactionEntryEntity toEntity(PaymentTransactionEntryDTO dto);

    PaymentTransactionEntryDTO toDto(PaymentTransactionEntryEntity entity);

    default PaymentTransactionEntryDTO toDtoWithTransaction(PaymentTransactionEntryEntity entity, PaymentTransactionDTO paymentTransactionDTO) {
        if (entity == null) {
            return null;
        }
        final PaymentTransactionEntryDTO paymentTransactionEntryDTO = toDto(entity);
        paymentTransactionEntryDTO.setPaymentTransactionDto(paymentTransactionDTO);
        return paymentTransactionEntryDTO;
    }
}
