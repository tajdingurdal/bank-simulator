package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.mapper.CardMapper;
import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper mapper;

    public CardService(final CardRepository cardRepository, final CardMapper mapper) {
        this.cardRepository = cardRepository;
        this.mapper = mapper;
    }

    public CardDTO findByCardNo(String cardNo) {
        return cardRepository.findByCardNo(cardNo).map(mapper::toDto).orElse(null);
    }

    public List<CardDTO> findByCardNoIn(List<String> cardNos) {
        return cardRepository.findByCardNoIn(cardNos).map(mapper::toDto).orElse(null);
    }

    public void saveAll(final List<CardEntity> cards) {
        cardRepository.saveAll(cards);
    }
}
