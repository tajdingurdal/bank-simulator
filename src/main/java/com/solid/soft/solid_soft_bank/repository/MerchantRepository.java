package com.solid.soft.solid_soft_bank.repository;

import com.solid.soft.solid_soft_bank.model.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    Optional<MerchantEntity> findByApiKey(String apikey);

    Optional<MerchantEntity> findByName(String name);

    Optional<MerchantEntity> findByWebSite(String website);

    Optional<MerchantEntity> findByNameAndWebSite(String name, String website);

    boolean existsByNameAndWebSite(String name, String website);
}
