package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTransactionEntryMapper extends BaseMapper<PaymentTransactionEntryDTO, PaymentTransactionEntryEntity> {

    default PaymentTransactionEntryDTO toDtoWithTransaction(PaymentTransactionEntryEntity entity, PaymentTransactionDTO paymentTransactionDTO) {
        if (entity == null) {
            return null;
        }
        final PaymentTransactionEntryDTO paymentTransactionEntryDTO = toDto(entity);
        paymentTransactionEntryDTO.setPaymentTransactionDto(paymentTransactionDTO);
        return paymentTransactionEntryDTO;
    }
}
