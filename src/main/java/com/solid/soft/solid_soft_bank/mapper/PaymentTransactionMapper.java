package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransactionDTO, PaymentTransactionEntity> {
}
