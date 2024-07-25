package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.AuthenticateEntity;
import com.solid.soft.solid_soft_bank.model.Card;
import com.solid.soft.solid_soft_bank.model.SubscribeResponseEntity;
import com.solid.soft.solid_soft_bank.model.UserPaymentProcess;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateRequestDTO;
import com.solid.soft.solid_soft_bank.model.dto.AuthenticateResponseDTO;
import com.solid.soft.solid_soft_bank.model.dto.UserPaymentProcessDTO;
import com.solid.soft.solid_soft_bank.repository.AuthenticateRepository;
import com.solid.soft.solid_soft_bank.utils.ResponseMessages;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AuthenticateService {

    private final AuthenticateRepository repository;

    private final AuthenticateRepository authenticateRepository;
    private final SubscribeService       subscribeService;
    private final UserCardService        userCardService;
    private final CardService cardService;

    public AuthenticateService(final AuthenticateRepository repository, final AuthenticateRepository authenticateRepository,
                               final SubscribeService subscribeService, final UserCardService userCardService,
                               final CardService cardService) {
        this.repository = repository;
        this.authenticateRepository = authenticateRepository;
        this.subscribeService = subscribeService;
        this.cardService = cardService;
    }

    public AuthenticateEntity findByBankTransactionCode(String bankTransactionCode) {
        return repository.findByBankTransactionCode(bankTransactionCode).orElseThrow();
    }

    public AuthenticateEntity findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public AuthenticateResponseDTO authenticatePrePayment(final AuthenticateRequestDTO authenticate) {

        final String merchantTransactionCode = authenticate.getMerchantTransactionCode();
        final SubscribeRequestEntity subscribeRequestEntity = subscribeService.findSubscribeRequestByMerchantTransactionCode(
                merchantTransactionCode);

        final SubscribeResponseEntity subscribeResponseEntity = subscribeService.findSubscribeResponseByMerchantTransactionCode(
                merchantTransactionCode);

        final AuthenticateResponseDTO response = new AuthenticateResponseDTO();

        if (!subscribeRequestEntity.getMerchantTransactionCode().equals(merchantTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            return response;
        }

        if (!subscribeRequestEntity.getApiKey().equals(authenticate.getApiKey())) {
            response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
            response.setStatus(false);
            return response;
        }

        final String bankTransactionCode = authenticate.getBankTransactionCode();
        if (!subscribeResponseEntity.getBankTransactionCode().equals(bankTransactionCode)) {
            response.setMessage(ResponseMessages.AUTHENTICATE_FAILED);
            response.setStatus(false);
            return response;
        }

        final AuthenticateEntity authenticateEntity = new AuthenticateEntity(authenticate.getAmount(), authenticate.getFailureRedirectURL(),
                                                                             authenticate.getSuccessRedirectURL(),
                                                                             authenticate.getCurrency(),
                                                                             ResponseMessages.AUTHENTICATE_SUCCESS,
                                                                             authenticate.getBankTransactionCode(),
                                                                             authenticate.getMerchantTransactionCode(),
                                                                             true,
                                                                             authenticate.getApiKey());

        authenticateRepository.save(authenticateEntity);

        response.setStatus(true);
        response.setMessage(ResponseMessages.AUTHENTICATE_SUCCESS);
        response.setBankTransactionCode(bankTransactionCode);
        response.setPaymentUrl("http://localhost:8080/bank/ui/payment-page?bankTransactionCode=" + bankTransactionCode);

        return response;
    }

    public String authenticatePaymentProcess(final String bankTransactionCode, final UserPaymentProcessDTO dto) throws InstanceAlreadyExistsException {
        final AuthenticateEntity           authenticateEntity = findByBankTransactionCode(bankTransactionCode);
        final Optional<UserPaymentProcess> userPaymentProcess           = userCardService.findByIdAndBankTransactionCode(authenticateEntity.getId(), bankTransactionCode);

        if (userPaymentProcess.isPresent()) {
            throw new InstanceAlreadyExistsException();
        }
        if (dto == null || dto.getCardNo() == null || dto.getName() == null || dto.getSurname() == null || dto.getCvc() == null || dto.getExpiredDate() == null) {
            return "";
        }

        final Card card = cardService.findByCardNo(dto.getCardNo());
        if (!card.getCvc().equals(dto.getCvc()) || !card.getExpiredDate().equals(dto.getExpiredDate()) || !card.getName().equals(dto.getName())
                || !card.getSurname().equals(dto.getSurname()) || !dto.getAmount().equals(card.getAmount()) || !dto.getCurrency().equals(card.getCurrency())) {
            return "";
        }

        final String[] expiredDateArr     = card.getExpiredDate().split("-");
        final Integer  monthOfExpiredDate = Integer.valueOf(expiredDateArr[0]);
        final Integer  yearOfExpiredDate  = Integer.valueOf(String.format("%o" + expiredDateArr[1], 20));

        if (yearOfExpiredDate < ZonedDateTime.now().getYear() && monthOfExpiredDate < ZonedDateTime.now().getMonthValue()) {
            return "";
        }

        if(card.getAmount() <= 0){
            return "";
        }

        return null;
    }
}
