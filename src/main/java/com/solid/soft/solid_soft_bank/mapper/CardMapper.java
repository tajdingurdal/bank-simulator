package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper extends BaseMapper<CardDTO, CardEntity>{
    @Mapping(target = "amount", source = "entity.balance")
    CardDTO toDto(CardEntity entity);
}
