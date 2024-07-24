package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.PreCheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreCheckoutRepository extends JpaRepository<PreCheckoutEntity, Long> {

}
