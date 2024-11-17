package com.etraveli.cardcostapi.controller;

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
     * Endpoint para calcular el costo de clearing basado en el número de tarjeta.
     *
     * @param cardNumber El número de la tarjeta.
     * @return El costo de clearing calculado.
     */
    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateClearingCost(@RequestParam String cardNumber) {
        BigDecimal cost = clearingCostService.calculateClearingCost(cardNumber);
        return new ResponseEntity<>(cost, HttpStatus.OK);
    }
}
