package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.ZonedDateTime;

@Service
public class AuthenticateService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    private static final String PAYMENT_URL = "http://localhost:8090/bank/checkout";
    private final SubscribeService subscribeService;
    private final CardService cardService;
    private final PaymentTransactionService paymentTransactionService;

    public AuthenticateService(final SubscribeService subscribeService,
                               final CardService cardService,
                               final PaymentTransactionService paymentTransactionService) {
        this.subscribeService = subscribeService;
        this.cardService = cardService;
        this.paymentTransactionService = paymentTransactionService;
    }

    public PaymentTransactionEntryDTO findByBankTransactionCode(String bankTransactionCode) {
        return paymentTransactionService.findByBankTransactionCodeAndType(bankTransactionCode, PaymentTransactionType.AUTHENTICATE);
    }

    public PaymentTransactionEntryDTO findById(Long id) {
        return paymentTransactionService.findByIdAndType(id, PaymentTransactionType.AUTHENTICATE);
    }

    public AuthenticateResponseDTO authenticatePrePayment(final AuthenticateRequestDTO dto) throws InstanceAlreadyExistsException {

        final String bankTransactionCode = dto.getBankTransactionCode();
        final CardDTO card = dto.getCard();

        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();
        final PaymentTransactionDTO paymentTransactionDtoFromDB = paymentTransactionService.findByBankTransactionCode(bankTransactionCode);

        AuthenticateResponseDTO comparedData = compareDTODataAndDBDataToValidate(dto, paymentTransactionDtoFromDB, bankTransactionCode, response);
        if (comparedData != null) {return comparedData;}

        final PaymentTransactionEntryEntity authenticateEntry = createAuthenticateEntry(dto, paymentTransactionDtoFromDB);
        final PaymentTransactionEntryEntity paymentTransactionEntryEntity = paymentTransactionService.saveEntry(authenticateEntry);

        response.setId(paymentTransactionEntryEntity.getId());
        response.setStatus(true);
        response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);
        response.setPaymentUrl(String.format(PAYMENT_URL + "?bankTransactionCode=%s", bankTransactionCode));

        log.debug("Pre-payment authentication process completed: Bank Transaction Code: {}, Payment URL: {}", bankTransactionCode, response.getPaymentUrl());

        return response;
    }

    public String authenticatePaymentProcess(final String bankTransactionCode, final CardDTO dto) {

        log.debug("Starting payment authentication process: Bank Transaction Code: {}, Card Number: {}",
                bankTransactionCode, dto.getCardNo());

        final PaymentTransactionEntryDTO authenticateEntry = findByBankTransactionCode(bankTransactionCode);

        if (authenticateEntry == null) {
            log.warn("Transaction not found for Bank Transaction Code: {}", bankTransactionCode);
            return ResponseMessages.transactionNotFound(bankTransactionCode);
        }

        String validateCardDetailsMessage = checkMissingCardDetails(dto);
        if (validateCardDetailsMessage != null) {
            log.warn("Card details validation failed: {}", validateCardDetailsMessage);
            return validateCardDetailsMessage;
        }

        final CardDTO cardDTOFromDB = cardService.findByCardNo(dto.getCardNo());

        String validationCardAgainstDBMessage = validateCardAgainstDatabase(cardDTOFromDB, dto);
        if (validationCardAgainstDBMessage != null) {
            log.warn("Card validation against database failed: {}", validationCardAgainstDBMessage);
            return validationCardAgainstDBMessage;
        }

        String validationMessage = validateCardExpirationDate(cardDTOFromDB);
        if (validationMessage != null) {
            log.warn("Card expiration date validation failed: {}", validationMessage);
            return validationMessage;
        }

        if (cardDTOFromDB.getAmount() <= 0) {
            log.warn("Insufficient balance for Card Number: {}", dto.getCardNo());
            return "Insufficient balance.";
        }

        log.debug("Payment authentication process completed successfully: Bank Transaction Code: {}", bankTransactionCode);

        return null;
    }

    private PaymentTransactionEntryEntity createAuthenticateEntry(final AuthenticateRequestDTO authenticateDto,
                                                                  final PaymentTransactionDTO paymentTransactionDTO) {
        final PaymentTransactionEntryEntity authenticateEntry = new PaymentTransactionEntryEntity();
        authenticateEntry.setAmount(authenticateDto.getAmount());
        authenticateEntry.setCurrency(authenticateDto.getCurrency());
        authenticateEntry.setFailedRedirectURL(authenticateDto.getFailureRedirectURL());
        authenticateEntry.setSuccessRedirectURL(authenticateDto.getSuccessRedirectURL());
        authenticateEntry.setResultMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
        authenticateEntry.setTransactionType(PaymentTransactionType.AUTHENTICATE);
        authenticateEntry.setStatus(true);
        authenticateEntry.setPaymentTransactionId(paymentTransactionDTO.getId());
        return authenticateEntry;
    }

    private AuthenticateResponseDTO compareDTODataAndDBDataToValidate(final AuthenticateRequestDTO dto,
                                                                      final PaymentTransactionDTO paymentTransactionDtoFromDB,
                                                                      final String bankTransactionCode,
                                                                      final AuthenticateResponseDTO response) {

        final String merchantTransactionCode = dto.getMerchantTransactionCode();
        final String apiKey = dto.getApiKey();

        if (paymentTransactionDtoFromDB == null) {
            return setErrorResponse(response, ResponseMessages.transactionNotFound(bankTransactionCode));
        }

        final MerchantDTO merchantDTOFromDB = paymentTransactionDtoFromDB.getMerchantEntity();
        final String merchantTransactionCodeFromDB = paymentTransactionDtoFromDB.getMerchantTransactionCode();
        final String bankTransactionCodeFromDB = paymentTransactionDtoFromDB.getBankTransactionCode();

        final PaymentTransactionEntryDTO subscribeEntry = subscribeService.findSubscribeEntryByPaymentTransactionId(paymentTransactionDtoFromDB.getId());

        if (subscribeEntry == null) {
            return setErrorResponse(response, ResponseMessages.subscribeRequired(merchantTransactionCode));
        }

        if (merchantDTOFromDB == null) {
            return setErrorResponse(response, ResponseMessages.merchantDoesntExist(bankTransactionCode));
        }

        if (!merchantTransactionCodeFromDB.equals(merchantTransactionCode)
                || !bankTransactionCodeFromDB.equals(bankTransactionCode)
                || !merchantDTOFromDB.getApiKey().equals(apiKey)) {

            return setErrorResponse(response,
                    ResponseMessages.bankAndMerchantAndApikeyAreNotValidCombinationError(merchantTransactionCode, bankTransactionCode, apiKey));
        }

        if (!subscribeEntry.getAmount().equals(dto.getAmount())) {
            return setErrorResponse(response, ResponseMessages.doesNotMatchAmountMessage(dto.getAmount()));
        }

        if (!subscribeEntry.getCurrency().equals(dto.getCurrency())) {
            return setErrorResponse(response, ResponseMessages.doesNotMatchCurrencyMessage(dto.getCurrency()));
        }

        return null;
    }

    private String checkMissingCardDetails(CardDTO dto) {
        if (dto == null) {
            return "CardDTO is null.";
        }
        if (dto.getCardNo() == null) {
            return "Card number is missing.";
        }
        if (dto.getName() == null) {
            return "Cardholder's name is missing.";
        }
        if (dto.getSurname() == null) {
            return "Cardholder's surname is missing.";
        }
        if (dto.getCvc() == null) {
            return "CVC code is missing.";
        }
        if (dto.getExpiredDate() == null) {
            return "Expiration date is missing.";
        }
        if (dto.getAmount() == null) {
            return "Amount is missing.";
        }
        if (dto.getCurrency() == null) {
            return "Currency is missing.";
        }
        return null;
    }


    private String validateCardAgainstDatabase(final CardDTO cardDB, final CardDTO request) {

        if (cardDB == null) {
            return "Card is empty.";
        }
        if (!cardDB.getCvc().equals(request.getCvc())) {
            return "CVC code does not match.";
        }
        if (!cardDB.getExpiredDate().equals(request.getExpiredDate())) {
            return "Expiration date does not match.";
        }
        if (!cardDB.getName().equals(request.getName())) {
            return "Cardholder's name does not match.";
        }
        if (!cardDB.getSurname().equals(request.getSurname())) {
            return "Cardholder's surname does not match.";
        }
        if (!request.getCurrency().equals(cardDB.getCurrency())) {
            return "Currency does not match.";
        }
        return null;
    }

    private String validateCardExpirationDate(CardDTO cardDTO) {
        final String[] expiredDateArr = cardDTO.getExpiredDate().split("/");
        final int monthOfExpiredDate = Integer.parseInt(expiredDateArr[0]);
        final Integer yearOfExpiredDate = Integer.valueOf(String.format("20%s", expiredDateArr[1]));

        if (yearOfExpiredDate < ZonedDateTime.now().getYear() ||
                (yearOfExpiredDate.equals(ZonedDateTime.now().getYear()) && monthOfExpiredDate < ZonedDateTime.now().getMonthValue())) {
            return "Card is expired.";
        }
        return null;
    }

    private AuthenticateResponseDTO setErrorResponse(AuthenticateResponseDTO response, String message) {
        response.setMessage(message);
        response.setStatus(false);
        log.warn(message);
        return response;
    }
}
