package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dto.BinlistResponse;
import com.etraveli.cardcostapi.entity.ClearingCost;
import com.etraveli.cardcostapi.service.IClearingCostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/clearing-cost")
@RequiredArgsConstructor
public class ClearingCostController {

    private final IClearingCostService clearingCostService;

    @PostMapping
    public ResponseEntity<ClearingCost> createClearingCost(@Valid @RequestBody ClearingCost clearingCost) {
        return new ResponseEntity<>(clearingCostService.saveClearingCost(clearingCost), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClearingCost>> getAllClearingCosts() {
        return ResponseEntity.ok(clearingCostService.findAll());
    }

    @GetMapping("/{countryCode}")
    public ResponseEntity<ClearingCost> getClearingCostByCountry(@PathVariable String countryCode) {
        return ResponseEntity.ok(clearingCostService.findByCountryCode(countryCode));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClearingCost> updateClearingCost(@PathVariable Long id, @Valid @RequestBody ClearingCost clearingCost) {
        return ResponseEntity.ok(clearingCostService.updateClearingCost(id, clearingCost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable Long id) {
        clearingCostService.deleteClearingCost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para calcular el costo basado en el número de tarjeta (PAN).
     * @param cardNumber El número de la tarjeta.
     * @return Una respuesta con el costo  calculado o un error 400 si el PAN no es válido.
     */
    @GetMapping("/payment-cards-cost")
    public ResponseEntity<Object> calculateClearingCost(@RequestParam String cardNumber) {
        try {
            // Validar el PAN antes de proceder
            if (!clearingCostService.isPanValid(cardNumber)) {
                return ResponseEntity.badRequest().body("Número de tarjeta inválido. Verifique el formato y longitud.");
            }
            BinlistResponse binlistResponse = clearingCostService.getCountryCodeFromCardNumber(cardNumber);
            BigDecimal cost = clearingCostService.calculateClearingCost(cardNumber);
            return ResponseEntity.ok().body(
                    new BinlistResponse.BinlistResponseWithCost(binlistResponse.getCountry().getAlpha2(), cost)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Número de tarjeta inválido.");
        }
    }
}