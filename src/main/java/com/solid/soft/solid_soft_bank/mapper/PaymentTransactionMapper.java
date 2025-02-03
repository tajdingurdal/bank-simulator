package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransactionDTO, PaymentTransactionEntity> {

    @Override
    @Mapping(target = "merchantId", source = "entity.merchantId")
    @Mapping(target = "merchantDTO", source = "entity.merchantEntity")
    PaymentTransactionDTO toDto(PaymentTransactionEntity entity);
}
