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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;

@Service
public class AuthenticateService extends BaseEntryService{

    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    @Value("${payment.url}")
    private String paymentUrl;
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

    public AuthenticateResponseDTO authenticatePrePayment(final AuthenticateRequestDTO request) throws InstanceAlreadyExistsException {

        final String bankTransactionCode = request.getBankTransactionCode();
        final PaymentTransactionDTO paymentTransactionDtoFromDB = paymentTransactionService.findByBankTransactionCode(bankTransactionCode);

        AuthenticateResponseDTO comparedData = compareDTODataAndDBDataToValidate(request, paymentTransactionDtoFromDB, bankTransactionCode);
        if (comparedData != null) {
            final PaymentTransactionEntryEntity authenticateEntry = createEntry(request.getAmount(), request.getCurrency(), request.getFailureRedirectURL(),
                    request.getSuccessRedirectURL(), paymentTransactionDtoFromDB.getId(), comparedData.getMessage(), false,
                    PaymentTransactionType.AUTHENTICATE);
            final PaymentTransactionEntryEntity savedAuthenticateEntry = paymentTransactionService.saveEntry(authenticateEntry);
            return createAuthenticateResponse(savedAuthenticateEntry.getId(), false, comparedData.getMessage(), bankTransactionCode, null);
        }

        if (Objects.nonNull(request.getCard())) {
            final String validationCardResult = cardService.processValidationCard(request.getCard());
            if (validationCardResult != null) {
                final PaymentTransactionEntryEntity authenticateEntry = createEntry(request.getAmount(), request.getCurrency(), request.getFailureRedirectURL(),
                        request.getSuccessRedirectURL(), paymentTransactionDtoFromDB.getId(), validationCardResult, false, PaymentTransactionType.AUTHENTICATE);
                final PaymentTransactionEntryEntity savedAuthenticateEntry = paymentTransactionService.saveEntry(authenticateEntry);
                return createAuthenticateResponse(savedAuthenticateEntry.getId(), false, validationCardResult, bankTransactionCode, null);
            }
        }

        final PaymentTransactionEntryEntity authenticateEntry = createEntry(request.getAmount(), request.getCurrency(), request.getFailureRedirectURL(),
                request.getSuccessRedirectURL(), paymentTransactionDtoFromDB.getId(), ResponseMessages.AUTHENTICATE_SUCCESS, true, PaymentTransactionType.AUTHENTICATE);
        final PaymentTransactionEntryEntity savedAuthenticateEntry = paymentTransactionService.saveEntry(authenticateEntry);

        log.debug("Pre-payment authentication process completed");

        return createAuthenticateResponse(savedAuthenticateEntry.getId(), true, ResponseMessages.AUTHENTICATE_SUCCESS, bankTransactionCode, String.format(paymentUrl + "?bankTransactionCode=%s", bankTransactionCode));
    }

    public String authenticatePaymentProcess(final String bankTransactionCode, final CardDTO dto) {

        final PaymentTransactionEntryDTO authenticateEntry = findByBankTransactionCode(bankTransactionCode);

        if (authenticateEntry == null) {
            log.warn("Transaction not found for Bank Transaction Code: {}", bankTransactionCode);
            return ResponseMessages.transactionNotFound(bankTransactionCode);
        }

        String checkMissingCardDetails = cardService.processValidationCard(dto);
        if (checkMissingCardDetails != null) {return checkMissingCardDetails;}

        log.debug("Payment authentication process completed successfully: Bank Transaction Code: {}", bankTransactionCode);

        return null;
    }

    private AuthenticateResponseDTO compareDTODataAndDBDataToValidate(final AuthenticateRequestDTO dto,
                                                                      final PaymentTransactionDTO paymentTransactionDtoFromDB,
                                                                      final String bankTransactionCode) {
        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();

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

    private AuthenticateResponseDTO setErrorResponse(AuthenticateResponseDTO response, String message) {
        response.setMessage(message);
        response.setStatus(false);
        log.warn(message);
        return response;
    }


    private AuthenticateResponseDTO createAuthenticateResponse(final Long entryId,
                                                               final boolean status,
                                                               final String message,
                                                               final String bankTransactionCode,
                                                               final String paymentUrl) {
        return new AuthenticateResponseDTO(entryId, bankTransactionCode, status, message, paymentUrl);
    }
}
