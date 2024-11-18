package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistDto;
import com.etraveli.cardcostapi.entity.ClearingCost;
import com.etraveli.cardcostapi.exception.ResourceNotFoundException;
import com.etraveli.cardcostapi.repository.ClearingCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClearingCostService implements IClearingCostService {

    private final ClearingCostRepository clearingCostRepository;
    private final BinlistService binlistService;

    /**
     * Saves a new clearing cost to the database.
     * @param clearingCost The cost to be saved.
     * @return The saved 'ClearingCost'.
     */
    @Override
    public ClearingCost saveClearingCost(ClearingCost clearingCost) {
        return clearingCostRepository.save(clearingCost);
    }

    /**
     * Updates an existing cost in the database.
     * @param id The ID of the cost to update.
     * @param clearingCost The updated cost data.
     * @return The updated 'ClearingCost'.
     */
    @Override
    public ClearingCost updateClearingCost(Long id, ClearingCost clearingCost) {
        ClearingCost existing = clearingCostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cost not found"));
        existing.setCountryCode(clearingCost.getCountryCode());
        existing.setCost(clearingCost.getCost());
        return clearingCostRepository.save(existing);
    }

    /**
     * Deletes a cost from the database by its ID.
     * @param id The ID of the cost to be deleted.
     */
    public void deleteClearingCost(Long id) {
        clearingCostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clearing cost not found for ID: " + id));
        clearingCostRepository.deleteById(id);
    }

    /**
     * Retrieves all costs from the database.
     * @return A list of all 'ClearingCost' entries.
     */
    @Override
    public List<ClearingCost> findAll() {
        return clearingCostRepository.findAll();
    }

    /**
     * Searches a cost by country code.
     * @param countryCode The country code to lookup.
     * @return 'ClearingCost' corresponding to the country.
     * @throws ResourceNotFoundException If no cost is found for the specified country.
     */

    @Override
    public ClearingCost findByCountryCode(String countryCode) {
        return clearingCostRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cost not found"));
    }

    /**
     * Calculates the clearing cost based on the BIN (first digits of the card).
     * @param cardNumber The card number to determine the issuing country.
     * @return The calculated cost.
     */
    @Override
    public BigDecimal calculateClearingCost(String cardNumber) {
        // Validate the PAN before proceeding
        if (!isPanValid(cardNumber)) {
            throw new IllegalArgumentException("Número de tarjeta inválido");
        }

        String countryCode = getCountryCodeFromCardNumber(cardNumber).getCountry().getAlpha2();
        ClearingCost clearingCost = clearingCostRepository.findByCountryCode(countryCode)
                .orElseGet(() -> getDefaultClearingCost(countryCode));
        return clearingCost.getCost();
    }

    /**
     * Retrieves the country code of the card using Binlist.
     * @param cardNumber The card number.
     * @return The issuing country code.
     */

    @Override
    public BinlistDto getCountryCodeFromCardNumber(String cardNumber) {
        return binlistService.getCountryCodeByCardNumber(cardNumber);
    }

    /**
     * Provides a default cost for unspecified countries (OTHERS).
     * @param countryCode The country code for which the cost needs to be determined.
     * @return 'ClearingCost' with the default cost.
     */

    private ClearingCost getDefaultClearingCost(String countryCode) {
        ClearingCost defaultCost = new ClearingCost();
        defaultCost.setCountryCode(countryCode);
        defaultCost.setCost(new BigDecimal("10.00"));
        return defaultCost;
    }

    /**
     * Valida si el PAN cumple con el formato correcto y pasa la validación de Luhn.
     * @param pan El número de tarjeta (PAN).
     * @return Verdadero si el PAN es válido, falso de lo contrario.
     */
    @Override
    public boolean isPanValid(String pan) {
        return isValidPanFormat(pan) && isValidLuhn(pan);
    }

    /**
     * Valida el formato del PAN (solo dígitos y longitud entre 8 y 19).
     * @param pan El número de tarjeta.
     * @return Verdadero si el formato es válido, falso de lo contrario.
     */
    public boolean isValidPanFormat(String pan) {
        if (pan == null || pan.length() < 8 || pan.length() > 19) {
            return false;
        }
        return pan.matches("\\d+");
    }

    /**
     * Algoritmo de Luhn para validar el PAN.
     * @param pan El número de tarjeta.
     * @return Verdadero si el PAN es válido según Luhn, falso de lo contrario.
     */
    public boolean isValidLuhn(String pan) {
        int nDigits = pan.length();
        int sum = 0;
        boolean isSecond = false;

        // Iterar de derecha a izquierda
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = pan.charAt(i) - '0';
            if (isSecond) {
                d = d * 2;
            }
            // Si el resultado de la multiplicación es mayor a 9, restamos 9
            sum += d > 9 ? d - 9 : d;
            isSecond = !isSecond;
        }
        return (sum % 10 == 0);
    }

}
