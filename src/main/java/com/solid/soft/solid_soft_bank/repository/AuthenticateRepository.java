package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.AuthenticateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticateRepository extends JpaRepository<AuthenticateEntity, Long> {

    Optional<AuthenticateEntity> findByBankTransactionCode(String bankTransactionCode);
}
