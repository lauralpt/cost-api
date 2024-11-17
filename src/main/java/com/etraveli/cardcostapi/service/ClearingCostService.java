package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistResponse;
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
     * Guarda un nuevo costo de clearing en la base de datos.
     *
     * @param clearingCost Costo a guardar.
     * @return 'ClearingCost' guardado.
     */
    @Override
    public ClearingCost saveClearingCost(ClearingCost clearingCost) {
        return clearingCostRepository.save(clearingCost);
    }

    /**
     * Actualiza un costo existente en la base de datos.
     *
     * @param id El ID del costo a actualizar.
     * @param clearingCost Datos actualizados del costo.
     * @return 'ClearingCost' actualizado.
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
     * Elimina un costo de la base de datos por su ID.
     * @param id ID del costo a eliminar.
     */
    @Override
    public void deleteClearingCost(Long id) {
        clearingCostRepository.deleteById(id);
    }

    /**
     * Obtiene todos los costos de la base de datos.
     * @return Una lista de todos los 'ClearingCost'.
     */
    @Override
    public List<ClearingCost> findAll() {
        return clearingCostRepository.findAll();
    }

    /**
     * Busca un costo por código de país.
     *
     * @param countryCode Código de país a buscar.
     * @return 'ClearingCost' correspondiente al país.
     * @throws ResourceNotFoundException Si no se encuentra un costo para el país especificado.
     */
    @Override
    public ClearingCost findByCountryCode(String countryCode) {
        return clearingCostRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cost not found"));
    }

    /**
     * Calcula el costo de clearing basado en el BIN (primeros dígitos de la tarjeta).
     * @param cardNumber El número de la tarjeta para determinar el país emisor.
     * @return El costo calculado.
     */
    @Override
    public BigDecimal calculateClearingCost(String cardNumber) {
        // Validar el PAN antes de proceder
        if (!isPanValid(cardNumber)) {
            throw new IllegalArgumentException("Número de tarjeta inválido");
        }

        String countryCode = getCountryCodeFromCardNumber(cardNumber).getCountry().getAlpha2();
        ClearingCost clearingCost = clearingCostRepository.findByCountryCode(countryCode)
                .orElseGet(() -> getDefaultClearingCost(countryCode));
        return clearingCost.getCost();
    }

    /**
     * Obtiene el código del país emisor de la tarjeta usando Binlist.
     *
     * @param cardNumber El número de tarjeta.
     * @return El código del país de emisión.
     */
    @Override
    public BinlistResponse getCountryCodeFromCardNumber(String cardNumber) {
        return binlistService.getCountryCodeByCardNumber(cardNumber);
    }

    /**
     * Proporciona un costo predeterminado para los países no especificados (OTHERS).
     * @param countryCode Código del país para el cual se requiere determinar el costo.
     * @return 'ClearingCost' con el costo predeterminado.
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
