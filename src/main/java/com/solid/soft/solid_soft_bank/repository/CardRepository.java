package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByCardNo(String cardNo);
}
