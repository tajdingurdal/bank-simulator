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

    public AuthenticateResponseDTO authenticatePrePayment(final AuthenticateRequestDTO authenticateDto) throws InstanceAlreadyExistsException {

        final String merchantTransactionCode = authenticateDto.getMerchantTransactionCode();
        final String bankTransactionCode = authenticateDto.getBankTransactionCode();

        final PaymentTransactionDTO paymentTransactionDto = paymentTransactionService.findByBankTransactionCode(bankTransactionCode);
        final MerchantDTO merchantDTO = paymentTransactionDto.getMerchantEntity();
        final PaymentTransactionEntryDTO subscribeEntry = subscribeService.findSubscribeEntryByPaymentTransactionId(paymentTransactionDto.getId());

        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();

        if (!paymentTransactionDto.getMerchantTransactionCode().equals(authenticateDto.getMerchantTransactionCode()) &&
                !paymentTransactionDto.getBankTransactionCode().equals(authenticateDto.getBankTransactionCode()) &&
                !paymentTransactionDto.getMerchantEntity().getApiKey().equals(authenticateDto.getApiKey())) {

            response.setMessage(ResponseMessages.bankAndMerchantAndApikeyAreNotValidCombinationError(merchantTransactionCode, bankTransactionCode));
            response.setStatus(false);
            return response;
        }

        if (subscribeEntry == null) {
            response.setMessage(ResponseMessages.subscribeRequired(merchantTransactionCode));
            response.setStatus(false);
            return response;
        }

        if (merchantDTO == null) {
            response.setMessage(ResponseMessages.merchantDoesntExist(bankTransactionCode));
            response.setStatus(false);
            return response;
        }

        if (!merchantDTO.getApiKey().equals(authenticateDto.getApiKey())) {
            response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
            response.setStatus(false);
            log.warn(ResponseMessages.AUTHENTICATE_FAILED);
            return response;
        }

        if (!paymentTransactionDto.getMerchantTransactionCode().equals(merchantTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            log.warn(ResponseMessages.AUTHENTICATE_FAILED);
            return response;
        }

        if (!paymentTransactionDto.getBankTransactionCode().equals(bankTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            log.warn(ResponseMessages.AUTHENTICATE_FAILED);
            return response;
        }

        if (!subscribeEntry.getAmount().equals(authenticateDto.getAmount())) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            log.warn(ResponseMessages.AUTHENTICATE_FAILED);
            return response;
        }

        if (!subscribeEntry.getCurrency().equals(authenticateDto.getCurrency())) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            log.warn(ResponseMessages.AUTHENTICATE_FAILED);
            return response;
        }

        final PaymentTransactionEntryEntity authenticateEntry = createAuthenticateEntry(authenticateDto, paymentTransactionDto);
        paymentTransactionService.saveEntry(authenticateEntry);

        response.setStatus(true);
        response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);
        response.setPaymentUrl(String.format(PAYMENT_URL + "?bankTransactionCode=%s", bankTransactionCode, bankTransactionCode));

        return response;
    }

    public String authenticatePaymentProcess(final String bankTransactionCode, final CardDTO dto) {

        final PaymentTransactionEntryDTO authenticateEntry = findByBankTransactionCode(bankTransactionCode);

        if (authenticateEntry == null) {
            return ResponseMessages.transactionNotFound(bankTransactionCode);
        }

        String validateCardDetailsMessage = validateCardDetails(dto);
        if (validateCardDetailsMessage != null) {
            return validateCardDetailsMessage;
        }

        final CardDTO cardDTO = cardService.findByCardNo(dto.getCardNo());

        String validationCardAgainstDBMessage = validateCardAgainstDatabase(cardDTO, dto);
        if (validationCardAgainstDBMessage != null) {
            return validationCardAgainstDBMessage;
        }

        String validationMessage = validateCardExpirationDate(cardDTO);
        if (validationMessage != null) {
            return validationMessage;
        }

        if (cardDTO.getAmount() <= 0) {
            return "Insufficient balance.";
        }

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
        authenticateEntry.setCreateDate(ZonedDateTime.now());
        return authenticateEntry;
    }

    private String validateCardDetails(CardDTO dto) {
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
            return String.format("Card with card number %s not found.", cardDB.getCardNo());
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
        final Integer monthOfExpiredDate = Integer.valueOf(expiredDateArr[0]);
        final Integer yearOfExpiredDate = Integer.valueOf(String.format("20%s", expiredDateArr[1]));

        if (yearOfExpiredDate < ZonedDateTime.now().getYear() ||
                (yearOfExpiredDate.equals(ZonedDateTime.now().getYear()) && monthOfExpiredDate < ZonedDateTime.now().getMonthValue())) {
            return "Card is expired.";
        }
        return null;
    }

}
