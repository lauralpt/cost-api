package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.entity.ClearingCost;

import java.math.BigDecimal;
import java.util.List;

public interface IClearingCostService {
    ClearingCost saveClearingCost(ClearingCost clearingCost);
    ClearingCost updateClearingCost(Long id, ClearingCost clearingCost);
    void deleteClearingCost(Long id);
    List<ClearingCost> findAll();
    ClearingCost findByCountryCode(String countryCode);

    BigDecimal calculateClearingCost(String cardNumber);
}
