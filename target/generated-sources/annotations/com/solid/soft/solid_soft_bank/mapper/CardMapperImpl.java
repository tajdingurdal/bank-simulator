package com.solid.soft.solid_soft_bank.mapper;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T21:51:31+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardEntity toEntity(CardDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CardEntity cardEntity = new CardEntity();

        cardEntity.setId( dto.getId() );
        cardEntity.setName( dto.getName() );
        cardEntity.setSurname( dto.getSurname() );
        cardEntity.setCardNo( dto.getCardNo() );
        cardEntity.setExpiredDate( dto.getExpiredDate() );
        cardEntity.setCvc( dto.getCvc() );
        cardEntity.setAmount( dto.getAmount() );
        cardEntity.setCurrency( dto.getCurrency() );

        return cardEntity;
    }

    @Override
    public CardDTO toDto(CardEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CardDTO cardDTO = new CardDTO();

        cardDTO.setId( entity.getId() );
        cardDTO.setName( entity.getName() );
        cardDTO.setSurname( entity.getSurname() );
        cardDTO.setCardNo( entity.getCardNo() );
        cardDTO.setExpiredDate( entity.getExpiredDate() );
        cardDTO.setCvc( entity.getCvc() );
        cardDTO.setAmount( entity.getAmount() );
        cardDTO.setCurrency( entity.getCurrency() );

        return cardDTO;
    }

    @Override
    public List<CardDTO> toDto(List<CardEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CardDTO> list = new ArrayList<CardDTO>( entities.size() );
        for ( CardEntity cardEntity : entities ) {
            list.add( toDto( cardEntity ) );
        }

        return list;
    }
}
