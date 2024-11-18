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

    @PostMapping("create-clearing-cost")
    public ResponseEntity<ClearingCost> createClearingCost(@Valid @RequestBody ClearingCost clearingCost) {
        return new ResponseEntity<>(clearingCostService.saveClearingCost(clearingCost), HttpStatus.CREATED);
    }

    @GetMapping("get-all-clearing-costs")
    public ResponseEntity<List<ClearingCost>> getAllClearingCosts() {
        return ResponseEntity.ok(clearingCostService.findAll());
    }

    @GetMapping("get-clearing-cost-by-country/{countryCode}")
    public ResponseEntity<ClearingCost> getClearingCostByCountry(@PathVariable String countryCode) {
        return ResponseEntity.ok(clearingCostService.findByCountryCode(countryCode));
    }

    @PutMapping("update-clearing-cost-by-country/{id}")
    public ResponseEntity<ClearingCost> updateClearingCost(@PathVariable Long id, @Valid @RequestBody ClearingCost clearingCost) {
        return ResponseEntity.ok(clearingCostService.updateClearingCost(id, clearingCost));
    }

    @DeleteMapping("delete-clearing-cost-by-country/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable Long id) {
        clearingCostService.deleteClearingCost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to calculate the cost based on the card number (PAN).
     * @param cardNumber The card number.
     * @return A response with the calculated cost or a 400 error if the PAN is not valid.
     */

    @GetMapping("/payment-cards-cost")
    public ResponseEntity<Object> calculateClearingCost(@RequestParam String cardNumber) {
        try {
            // Validate the PAN before proceeding
            if (!clearingCostService.isPanValid(cardNumber)) {
                return ResponseEntity.badRequest().body("Invalid card number. Please check the format and length.");
            }
            BinlistResponse binlistResponse = clearingCostService.getCountryCodeFromCardNumber(cardNumber);
            BigDecimal cost = clearingCostService.calculateClearingCost(cardNumber);
            return ResponseEntity.ok().body(
                    new BinlistResponse.BinlistResponseWithCost(binlistResponse.getCountry().getAlpha2(), cost)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid card number.");
        }
    }
}