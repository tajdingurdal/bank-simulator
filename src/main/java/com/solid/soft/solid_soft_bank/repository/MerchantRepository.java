package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    Optional<MerchantEntity> findByApiKey(String apikey);

    Optional<MerchantEntity> findByName(String name);

    Optional<MerchantEntity> findByWebSite(String website);
}
