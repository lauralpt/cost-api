package com.etraveli.cardcostapi.service;

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
     *
     * @param cardNumber El número de la tarjeta para determinar el país emisor.
     * @return El costo de clearing calculado.
     */
    public BigDecimal calculateClearingCost(String cardNumber) {

        String countryCode = binlistService.getCountryCodeByCardNumber(cardNumber);
        ClearingCost clearingCost = clearingCostRepository.findByCountryCode(countryCode)
                .orElseGet(() -> getDefaultClearingCost(countryCode));

        return clearingCost.getCost();
    }

    /**
     * Proporciona un costo predeterminado para los países no especificados (OTHERS).
     *
     * @param countryCode Código del país para el cual se requiere determinar el costo.
     * @return 'ClearingCost' con el costo predeterminado.
     */
    private ClearingCost getDefaultClearingCost(String countryCode) {
        ClearingCost defaultCost = new ClearingCost();
        defaultCost.setCountryCode(countryCode);
        defaultCost.setCost(new BigDecimal("10.00"));
        return defaultCost;
    }
}
