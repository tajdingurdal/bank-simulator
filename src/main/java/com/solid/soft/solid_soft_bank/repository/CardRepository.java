package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByCardNo(String cardNo);

    Optional<List<CardEntity>> findByCardNoIn(List<String> cardsNo);

    @Modifying
    @Query(value = "UPDATE CardEntity ce SET ce.balance = ce.balance - :amount, ce.lastTransactionTime = :now WHERE ce.cardNo = :cardNo")
    int decreaseBalance(@Param("cardNo") String cardNo, @Param("amount") Double amount, @Param("now") Instant now);
}
