package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardEntity toEntity(CardDTO dto);
    CardDTO toDto(CardEntity entity);
}
