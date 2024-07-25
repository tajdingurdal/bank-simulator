package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.Card;
import com.solid.soft.solid_soft_bank.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(final CardRepository cardRepository) {this.cardRepository = cardRepository;}

    public Card findByCardNo(String cardNo) {
        return cardRepository.findByCardNo(cardNo).orElseThrow();
    }
}
