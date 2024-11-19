package com.etraveli.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the response from the Binlist API.
 * The main purpose of this class is to encapsulate the information received from the Binlist API,
 * including the details about the country associated with a card.
 * It contains a nested class {@link Country} that holds country-specific information, and another nested
 * response class {@link BinlistResponseWithCost} that combines the country code with the associated clearing cost.
 * The class is annotated with Lombok's {@code @Data} to automatically generate boilerplate code
 *
 * @see lombok.Data
 */
@Data
public class BinlistDto {
    private Country country;

    @Data
    public static class Country {
        private String alpha2;
    }

    /**
     * Data Transfer Object (DTO) representing the response with country code and associated clearing cost.
     * This class is used to represent the response that includes both the ISO country code and the clearing cost
     * in dollars. It is intended to be used in scenarios where both the card's country information
     * and its clearing cost are required.
     * It is annotated with {@code @Data} and {@code @AllArgsConstructor} from Lombok for concise code,
     * as well as with Swagger's {@code @Schema} annotations for API documentation.
     */
    @Data
    @AllArgsConstructor
    @Schema(description = "Response containing the country code and the associated clearing cost.")
    public static class BinlistResponseWithCost {
        @Schema(description = "ISO country code of the card issuer", example = "US")
        @NotNull(message = "Country code cannot be null")
        @Size(min = 2, max = 2, message = "Country code must be of length 2")
        private String country;

        @NotNull(message = "Cost cannot be null")
        @Schema(description = "Clearing cost in dollars", example = "5.00")
        private BigDecimal cost;
    }
}
