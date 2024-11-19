package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dto.BinlistDto;
import com.etraveli.cardcostapi.entity.ClearingCost;
import com.etraveli.cardcostapi.service.IClearingCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/clearing-cost")
@RequiredArgsConstructor
@Tag(name = "Clearing Cost API", description = "API for calculating payment card clearing costs.")
public class ClearingCostController {

    private final IClearingCostService clearingCostService;
    private static final Logger logger = LoggerFactory.getLogger(ClearingCostController.class);


    @PostMapping("create-clearing-cost")
    @Operation(summary = "Create a new clearing cost",
            description = "Creates a new clearing cost entry for a specified country and cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clearing cost successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClearingCost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ClearingCost> createClearingCost(
            @Parameter(description = "ClearingCost object containing country code and cost information", required = true)
            @Valid @RequestBody ClearingCost clearingCost) {
        return new ResponseEntity<>(clearingCostService.saveClearingCost(clearingCost), HttpStatus.CREATED);
    }


    @GetMapping("get-all-clearing-costs")
    @Operation(summary = "Retrieve all clearing costs",
            description = "Returns a list of all clearing costs available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clearing costs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClearingCost.class)))
    })
    public ResponseEntity<List<ClearingCost>> getAllClearingCosts() {
        return ResponseEntity.ok(clearingCostService.findAll());
    }

    @GetMapping("get-clearing-cost-by-country/{countryCode}")
    @Operation(summary = "Retrieve clearing cost by country code",
            description = "Returns the clearing cost for a given country code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClearingCost.class))),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found for the specified country code",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ClearingCost> getClearingCostByCountry(
            @Parameter(description = "The ISO country code for which to retrieve the clearing cost", example = "US", required = true)
            @PathVariable String countryCode) {
        return ResponseEntity.ok(clearingCostService.findByCountryCode(countryCode));
    }


    @PutMapping("update-clearing-cost-by-country/{id}")
    @Operation(summary = "Update a clearing cost by ID",
            description = "Updates the clearing cost entry for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClearingCost.class))),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found for the specified ID",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ClearingCost> updateClearingCost(
            @Parameter(description = "The ID of the clearing cost to be updated", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "ClearingCost object containing the updated country code and cost", required = true)
            @Valid @RequestBody ClearingCost clearingCost) {
        return ResponseEntity.ok(clearingCostService.updateClearingCost(id, clearingCost));
    }

    @DeleteMapping("delete-clearing-cost-by-country/{id}")
    @Operation(summary = "Delete a clearing cost by ID",
            description = "Deletes the clearing cost entry for the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clearing cost deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found for the specified ID",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteClearingCost(
            @Parameter(description = "The ID of the clearing cost to be deleted", example = "1", required = true)
            @PathVariable Long id) {
        clearingCostService.deleteClearingCost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to calculate the cost based on the card number (PAN).
     * @param cardNumber The card number.
     * @return A response with the calculated cost or a 400 error if the PAN is not valid.
     */
    @GetMapping("/payment-cards-cost")
    @Operation(summary = "Calculate the clearing cost of a payment card",
            description = "Returns the clearing cost based on the country of the card. Uses Binlist to obtain " +
                    "information about the issuing country from the card number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost calculated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BinlistDto.BinlistResponseWithCost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card number or error obtaining information",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "429", description = "Too many requests",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> calculateClearingCost(
            @Parameter(description = "The payment card number (PAN). It should have between 8 and 19 digits.",
                    example = "45717360", required = true)
            @RequestParam @Pattern(regexp = "^[0-9]{8,19}$", message = "Card number must be between 8 and 19 digits.") String cardNumber) {

        try {
            if (!clearingCostService.isPanValid(cardNumber)) {
                return ResponseEntity.badRequest().body("Invalid card number. Please check the format and length.");
            }

            BinlistDto binlistResponse = clearingCostService.getCountryCodeFromCardNumber(cardNumber);
            BigDecimal cost = clearingCostService.calculateClearingCost(cardNumber);

            String maskedCardNumber = maskCardNumber(cardNumber);
            logger.info("Clearing cost calculated for card ending with: {}", maskedCardNumber);

            return ResponseEntity.ok().body(
                    new BinlistDto.BinlistResponseWithCost(binlistResponse.getCountry().getAlpha2(), cost)
            );
        } catch (IllegalArgumentException e) {
            logger.error("Invalid card number: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid card number.");
        } catch (Exception e) {
            logger.error("Error calculating clearing cost: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    /**
     * Masks a credit card number by replacing all but the last four digits with asterisks.
     * For card numbers with 4 or fewer characters, the original card number is returned as-is.
     * For longer card numbers, the result will be formatted as "**** **** **** XXXX",
     * where "XXXX" represents the last four digits of the input.
     *
     * @param cardNumber the credit card number to be masked. Must not be null.
     * @return the masked card number, with only the last four digits visible.
     * @throws NullPointerException if {@code cardNumber} is null.
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}