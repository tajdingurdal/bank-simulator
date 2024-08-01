package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.mapper.PaymentTransactionEntryMapper;
import com.solid.soft.solid_soft_bank.mapper.PaymentTransactionMapper;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntity;
import com.solid.soft.solid_soft_bank.model.PaymentTransactionEntryEntity;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionDTO;
import com.solid.soft.solid_soft_bank.model.dto.PaymentTransactionEntryDTO;
import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionEntryRepository;
import com.solid.soft.solid_soft_bank.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentTransactionEntryRepository entryRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final PaymentTransactionEntryMapper paymentTransactionEntryMapper;

    public PaymentTransactionService(final PaymentTransactionRepository paymentTransactionRepository,
                                     final PaymentTransactionMapper paymentTransactionMapper,
                                     final PaymentTransactionEntryRepository entryRepository,
                                     final PaymentTransactionEntryMapper paymentTransactionEntryMapper) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
        this.entryRepository = entryRepository;
        this.paymentTransactionEntryMapper = paymentTransactionEntryMapper;
    }

    public PaymentTransactionEntryEntity saveEntry(PaymentTransactionEntryEntity entity) throws InstanceAlreadyExistsException {

        if (Objects.nonNull(entity.getId())) {
            throw new IllegalStateException("New PaymentTransactionEntryEntity cannot have an ID.");
        }

        if (findSubscribeEntryByPaymentTransactionIdAndType(entity.getPaymentTransactionId(), PaymentTransactionType.AUTHENTICATE) != null) {
            throw new InstanceAlreadyExistsException("PaymentTransactionEntryEntity already exists.");
        }

        return entryRepository.save(entity);
    }

    public PaymentTransactionEntity savePaymentTransaction(PaymentTransactionEntity entity) throws IllegalStateException {

        if (Objects.nonNull(entity.getId())) {
            throw new IllegalStateException("New PaymentTransactionEntryEntity cannot have an ID.");
        }
        return paymentTransactionRepository.save(entity);
    }


    public PaymentTransactionDTO findByBankTransactionCode(String bankTransactionCode) {
        final Optional<PaymentTransactionEntity> entity = paymentTransactionRepository.findByBankTransactionCode(bankTransactionCode);
        return entity.map(paymentTransactionMapper::toDto).orElse(null);
    }

    public PaymentTransactionEntryDTO findByMerchantTransactionCodeAndType(String merchantTransactionCode, PaymentTransactionType type) {
        final Optional<PaymentTransactionEntryEntity> entity = entryRepository.findByMerchantTransactionCodeAndTransactionType(merchantTransactionCode, type);
        return entity.map(paymentTransactionEntryMapper::toDto).orElse(null);
    }

    public PaymentTransactionEntryDTO findByBankTransactionCodeAndType(String bankTransactionCode, PaymentTransactionType type) {
        return entryRepository.findByBankTransactionCodeAndTransactionType(bankTransactionCode, type)
                              .map(entity -> paymentTransactionEntryMapper.toDtoWithTransaction(entity,
                                      paymentTransactionMapper.toDto(entity.getPaymentTransaction())))
                              .orElse(null);
    }

    public PaymentTransactionEntryDTO findByIdAndType(Long id, PaymentTransactionType type) {
        final Optional<PaymentTransactionEntryEntity> entity = entryRepository.findByIdAndTransactionType(id, type);
        return entity.map(paymentTransactionEntryMapper::toDto).orElse(null);
    }

    public PaymentTransactionEntryDTO findSubscribeEntryByPaymentTransactionIdAndType(final Long paymentTransactionId,
                                                                                      final PaymentTransactionType paymentTransactionType) {
        final Optional<PaymentTransactionEntryEntity> entity = entryRepository.findByPaymentTransactionIdAndTransactionType(paymentTransactionId,
                paymentTransactionType);
        return entity.map(paymentTransactionEntryMapper::toDto).orElse(null);
    }
}
