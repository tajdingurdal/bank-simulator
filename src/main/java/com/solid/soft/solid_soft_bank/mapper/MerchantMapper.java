package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantMapper extends BaseMapper<MerchantDTO, MerchantEntity> {
}
