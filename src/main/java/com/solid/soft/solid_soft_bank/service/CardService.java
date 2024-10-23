package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.mapper.CardMapper;
import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.enums.CardStatusType;
import com.solid.soft.solid_soft_bank.model.enums.CardType;
import com.solid.soft.solid_soft_bank.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
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

    @Transactional
    public void updateBalanceAndLastTransactionTime(final String cardNo, final Double amount) {
        cardRepository.updateBalanceAndLastTransactionTime(cardNo, amount, Instant.now());
        log.debug("Balance and Last Transaction Time of card whose cardNo {} has been updated!", cardNo);
    }

    public String processValidationCard(final CardDTO request) {
        String checkMissingCardDetails = checkMissingCardDetails(request);
        if (checkMissingCardDetails != null) {
            return setErrorResponse(String.format("Card details validation failed: %s", checkMissingCardDetails));
        }

        final String cardNo = request.getCardNo();
        final CardDTO cardDTOFromDB = findByCardNo(cardNo);

        String validationCardAgainstDBMessage = validateCardAgainstDatabase(cardDTOFromDB, request);
        if (validationCardAgainstDBMessage != null) {
            return setErrorResponse(String.format("Card validation against database failed: %s", validationCardAgainstDBMessage));
        }

        String validationMessage = validateCardExpirationDate(cardDTOFromDB);
        if (validationMessage != null) {
            return setErrorResponse(String.format("Card expiration date validation failed: %s", validationMessage));
        }

        if (cardDTOFromDB.getAmount() <= 0) {
            return setErrorResponse(String.format("Insufficient balance for Card Number: %s", cardNo));
        }

        if (cardDTOFromDB.getStatus() != CardStatusType.ACTIVE) {
            return setErrorResponse(String.format("Card is not active for Card Number: %s", cardNo));
        }

        if (cardDTOFromDB.getFraudRiskScore() > 50) {
            return setErrorResponse(String.format("High fraud risk for Card Number: %s", cardNo));
        }

        if (request.getAmount() > cardDTOFromDB.getSpendingLimit()) {
            return setErrorResponse(String.format("Spending limit exceeded for Card Number: %s", cardNo));
        }

        if (cardDTOFromDB.getCardType() == CardType.CREDIT && request.getAmount() > (cardDTOFromDB.getCreditLimit() - cardDTOFromDB.getOutstandingDebt())) {
            return setErrorResponse(String.format("Credit limit exceeded for Card Number: %s", cardNo));
        }

        return null;
    }


    private String checkMissingCardDetails(CardDTO dto) {
        if (dto == null) {
            return setErrorResponse("Card is empty.");
        }
        if (dto.getCardNo() == null) {
            return setErrorResponse("Card number is missing.");
        }
        if (dto.getName() == null) {
            return setErrorResponse("Cardholder's name is missing.");
        }
        if (dto.getSurname() == null) {
            return setErrorResponse("Cardholder's surname is missing.");
        }
        if (dto.getCvc() == null) {
            return setErrorResponse("CVC code is missing.");
        }
        if (dto.getExpiredDate() == null) {
            return setErrorResponse("Expiration date is missing.");
        }
        if (dto.getAmount() == null) {
            return setErrorResponse("Amount is missing.");
        }
        if (dto.getCurrency() == null) {
            return setErrorResponse("Currency is missing.");
        }
        return null;
    }

    private String validateCardAgainstDatabase(final CardDTO cardDB, final CardDTO request) {

        if (cardDB == null) {
            return setErrorResponse("Card is empty.");
        }
        if (!cardDB.getCvc().equals(request.getCvc())) {
            return setErrorResponse("CVC code does not match.");
        }
        if (!cardDB.getExpiredDate().equals(request.getExpiredDate())) {
            return setErrorResponse("Expiration date does not match.");
        }
        if (!cardDB.getName().equals(request.getName())) {
            return setErrorResponse("Cardholder's name does not match.");
        }
        if (!cardDB.getSurname().equals(request.getSurname())) {
            return setErrorResponse("Cardholder's surname does not match.");
        }
        if (!request.getCurrency().equals(cardDB.getCurrency())) {
            return setErrorResponse("Currency does not match.");
        }
        return null;
    }

    private String validateCardExpirationDate(CardDTO cardDTO) {
        final String[] expiredDateArr = cardDTO.getExpiredDate().split("/");
        final int monthOfExpiredDate = Integer.parseInt(expiredDateArr[0]);
        final Integer yearOfExpiredDate = Integer.valueOf(String.format("20%s", expiredDateArr[1]));

        if (yearOfExpiredDate < ZonedDateTime.now().getYear() ||
                (yearOfExpiredDate.equals(ZonedDateTime.now().getYear()) && monthOfExpiredDate < ZonedDateTime.now().getMonthValue())) {
            return setErrorResponse("Card is expired.");
        }
        return null;
    }

    private String setErrorResponse(String message) {
        log.warn("Card | Validation failed: {}", message);
        return message;
    }
}
