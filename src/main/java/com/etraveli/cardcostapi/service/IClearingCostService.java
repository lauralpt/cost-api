package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistDto;
import com.etraveli.cardcostapi.entity.ClearingCost;

import java.math.BigDecimal;
import java.util.List;

public interface IClearingCostService {
    ClearingCost saveClearingCost(ClearingCost clearingCost);
    ClearingCost updateClearingCost(Long id, ClearingCost clearingCost);
    void deleteClearingCost(Long id);
    boolean isPanValid(String pan);
    List<ClearingCost> findAll();
    ClearingCost findByCountryCode(String countryCode);
    BinlistDto getCountryCodeFromCardNumber(String cardNumber);
    BigDecimal calculateClearingCost(String cardNumber);
}
