package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.entity.ClearingCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link ClearingCost} entities.
 *This repository extends {@link JpaRepository}, providing a wide range of methods
 * for CRUD operations and custom query execution for {@link ClearingCost} entities.
 * It is annotated with {@code @Repository}, which indicates that it is a Spring-managed
 * component that acts as a Data Access Object (DAO).
 * This interface includes a custom method to find a {@link ClearingCost} entity
 * by its country code.
 *
 * @see org.springframework.stereotype.Repository
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCost, Long> {
    Optional<ClearingCost> findByCountryCode(String countryCode);
}

