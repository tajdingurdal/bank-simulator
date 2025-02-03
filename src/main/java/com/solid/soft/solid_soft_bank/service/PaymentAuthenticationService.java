package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.CardDTO;
import com.solid.soft.solid_soft_bank.model.dto.MerchantDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationRequest;
import com.solid.soft.solid_soft_bank.resource.dto.AuthenticationResponse;
import com.solid.soft.solid_soft_bank.service.dto.CaptureRequest;
import com.solid.soft.solid_soft_bank.service.dto.OtpValidationResult;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;

import static com.solid.soft.solid_soft_bank.model.enums.CallbackStatus.FAILURE;
import static com.solid.soft.solid_soft_bank.model.enums.CallbackStatus.SUCCESS;

@Service
public class PaymentAuthenticationService extends BaseEntryService {

    private static final Logger log = LoggerFactory.getLogger(PaymentAuthenticationService.class);

    private final PaymentInitializationService paymentInitializationService;
    private final CardService cardService;
    private final PaymentTransactionService paymentTransactionService;
    private final CallBackService callBackService;
    private final CaptureService captureService;

    @Value("${application.payment.url}")
    private String paymentUrl;
    @Value("${application.temporary.otp}")
    public String temporaryOtp;
    @Value("${application.temporary.threshold}")
    public double otpThreshold;

    public PaymentAuthenticationService(final PaymentInitializationService paymentInitializationService,
                                        final CardService cardService,
                                        final PaymentTransactionService paymentTransactionService, CallBackService callBackService, CaptureService captureService) {
        this.paymentInitializationService = paymentInitializationService;
        this.cardService = cardService;
        this.paymentTransactionService = paymentTransactionService;
        this.callBackService = callBackService;
        this.captureService = captureService;
    }

    public PaymentTransactionEntryDTO findByBankTransactionCode(String bankTransactionCode) {
        return paymentTransactionService.findByBankTransactionCodeAndType(bankTransactionCode, PaymentTransactionType.AUTHENTICATE);
    }

    public PaymentTransactionEntryDTO findById(Long id) {
        return paymentTransactionService.findByIdAndType(id, PaymentTransactionType.AUTHENTICATE);
    }

    public AuthenticationResponse authenticatePrePayment(final AuthenticationRequest request) throws InstanceAlreadyExistsException {

        final String bankTransactionCode = request.getBankTransactionCode();
        final PaymentTransactionDTO paymentTransactionDtoFromDB = paymentTransactionService.findByBankTransactionCode(bankTransactionCode);

        AuthenticationResponse comparedData = compareDTODataAndDBDataToValidate(request, paymentTransactionDtoFromDB, bankTransactionCode);

        if (comparedData != null) {
            return handleFailedAuthentication(request, paymentTransactionDtoFromDB.getId(), comparedData.getMessage(), bankTransactionCode);
        }

        if (Objects.nonNull(request.getCard())) {
            final String validationCardResult = cardService.processValidationCard(request.getCard());
            if (validationCardResult != null) {
                return handleFailedAuthentication(request, paymentTransactionDtoFromDB.getId(), validationCardResult, bankTransactionCode);
            }
        }

        final PaymentTransactionEntryEntity authenticateEntry = createEntry(
                request.getAmount(),
                request.getCurrency(),
                paymentTransactionDtoFromDB.getId(),
                ResponseMessages.AUTHENTICATE_SUCCESS,
                true,
                PaymentTransactionType.AUTHENTICATE);

        final PaymentTransactionEntryEntity savedAuthenticateEntry = paymentTransactionService.saveEntry(authenticateEntry);

        log.debug("Pre-payment authentication process completed");

        boolean otpRequired = request.getAmount() >= otpThreshold;

        String paymentUrlWithParams = otpRequired ? String.format(paymentUrl + "?bankTransactionCode=%s", bankTransactionCode) : null;
        String message = otpRequired ? ResponseMessages.AUTHENTICATE_SUCCESS : ResponseMessages.AUTHENTICATE_SUCCESS_AND_OTP_NOT_REQUIRED;

        if (!otpRequired) {
            processSuccessfulPayment(
                    paymentTransactionDtoFromDB.getMerchantDTO(),
                    paymentTransactionDtoFromDB.getMerchantTransactionCode(),
                    paymentTransactionDtoFromDB.getBankTransactionCode(),
                    request.getCard().getCardNo(),
                    request.getAmount()
            );
        }

        return createAuthenticateResponse(
                savedAuthenticateEntry.getId(),
                true,
                message,
                bankTransactionCode,
                paymentUrlWithParams,
                otpRequired
        );
    }

    private AuthenticationResponse handleFailedAuthentication(AuthenticationRequest request, Long paymentTransactionId, String message, String bankTransactionCode) throws InstanceAlreadyExistsException {
        final PaymentTransactionEntryEntity authenticateEntry = createEntry(request.getAmount(), request.getCurrency(), paymentTransactionId, message, false, PaymentTransactionType.AUTHENTICATE);
        final PaymentTransactionEntryEntity savedAuthenticateEntry = paymentTransactionService.saveEntry(authenticateEntry);
        return createAuthenticateResponse(savedAuthenticateEntry.getId(), false, message, bankTransactionCode, null, true);
    }

    public String authenticatePaymentProcess(final String bankTransactionCode, final CardDTO dto) {

        final PaymentTransactionEntryDTO authenticateEntry = findByBankTransactionCode(bankTransactionCode);

        if (authenticateEntry == null) {
            log.warn("Transaction not found for Bank Transaction Code: {}", bankTransactionCode);
            return ResponseMessages.transactionNotFound(bankTransactionCode);
        }

        String checkMissingCardDetails = cardService.processValidationCard(dto);
        if (checkMissingCardDetails != null) {
            return checkMissingCardDetails;
        }

        log.debug("Payment authentication process completed successfully: Bank Transaction Code: {}", bankTransactionCode);

        return null;
    }

    private AuthenticationResponse compareDTODataAndDBDataToValidate(final AuthenticationRequest dto,
                                                                     final PaymentTransactionDTO paymentTransactionDtoFromDB,
                                                                     final String bankTransactionCode) {
        final AuthenticationResponse response = new AuthenticationResponse();

        final String merchantTransactionCode = dto.getMerchantTransactionCode();
        final String apiKey = dto.getApiKey();
        CardDTO card = dto.getCard();

        if (paymentTransactionDtoFromDB == null) {
            return setErrorResponse(response, ResponseMessages.transactionNotFound(bankTransactionCode));
        }

        if (card == null) {
            return setErrorResponse(response, ResponseMessages.cardRequired(merchantTransactionCode));
        }

        final MerchantDTO merchantDTOFromDB = paymentTransactionDtoFromDB.getMerchantDTO();
        final String merchantTransactionCodeFromDB = paymentTransactionDtoFromDB.getMerchantTransactionCode();
        final String bankTransactionCodeFromDB = paymentTransactionDtoFromDB.getBankTransactionCode();

        final PaymentTransactionEntryDTO subscribeEntry = paymentInitializationService.findSubscribeEntryByPaymentTransactionId(paymentTransactionDtoFromDB.getId());

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

    private AuthenticationResponse setErrorResponse(AuthenticationResponse response, String message) {
        response.setMessage(message);
        response.setStatus(false);
        log.warn(message);
        return response;
    }


    public AuthenticationResponse createAuthenticateResponse(final Long entryId,
                                                             final boolean status,
                                                             final String message,
                                                             final String bankTransactionCode,
                                                             final String paymentUrl,
                                                             boolean otpRequired) {
        return new AuthenticationResponse(entryId, bankTransactionCode, status, message, paymentUrl, otpRequired);
    }

    public OtpValidationResult validateOtp(String otpCode, String bankTransactionCode, String cardNo) {
        OtpValidationResult result = new OtpValidationResult();

        final PaymentTransactionEntryDTO authenticateEntry = findByBankTransactionCode(bankTransactionCode);
        if (authenticateEntry == null) {
            throw new EntityNotFoundException(String.format("Payment Transaction not found by Bank Transaction Code %s", bankTransactionCode));
        }

        MerchantDTO merchant = authenticateEntry.getPaymentTransactionDto().getMerchantDTO();
        String merchantTransactionCode = authenticateEntry.getPaymentTransactionDto().getMerchantTransactionCode();
        Double amount = authenticateEntry.getAmount();

        result.setMerchant(merchant);

        if (amount < otpThreshold) {
            result.setValid(true);
            result.setAmount(amount);
            processSuccessfulPayment(merchant, merchantTransactionCode, bankTransactionCode, cardNo, amount);
            return result;
        }

        if (!otpCode.equals(temporaryOtp)) {
            result.setValid(false);
            callBackService.sendCallBack(merchant, FAILURE, merchantTransactionCode);
            return result;
        }

        result.setValid(true);
        result.setAmount(amount);
        processSuccessfulPayment(merchant, merchantTransactionCode, bankTransactionCode, cardNo, amount);
        return result;
    }

    private void processSuccessfulPayment(MerchantDTO merchant, String merchantTransactionCode, String bankTransactionCode, String cardNo, Double amount) {
        boolean captured = captureService.capture(new CaptureRequest(bankTransactionCode, cardNo, amount, merchant));
        if (captured) {
            callBackService.sendCallBack(merchant, SUCCESS, merchantTransactionCode);
        }
    }
}
