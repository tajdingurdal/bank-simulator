package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.UserPaymentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserPaymentProcess, Long> {

    Optional<UserPaymentProcess> findByIdAndBankTransactionCode(long id, String bankTransactionCode);
}
