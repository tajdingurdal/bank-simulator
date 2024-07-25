package com.solid.soft.solid_soft_bank.service;

import com.solid.soft.solid_soft_bank.model.UserPaymentProcess;
import com.solid.soft.solid_soft_bank.repository.UserCardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCardService {

    private final UserCardRepository userCardRepository;

    public UserCardService(final UserCardRepository userCardRepository) {this.userCardRepository = userCardRepository;}

    public Optional<UserPaymentProcess> findByIdAndBankTransactionCode(final Long id, String bankTransactionCode) {
        return userCardRepository.findByIdAndBankTransactionCode(id, bankTransactionCode);
    }
}
