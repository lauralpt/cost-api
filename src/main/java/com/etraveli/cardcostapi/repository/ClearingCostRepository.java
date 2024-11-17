package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.entity.ClearingCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCost, Long> {
    Optional<ClearingCost> findByCountryCode(String countryCode);
}

