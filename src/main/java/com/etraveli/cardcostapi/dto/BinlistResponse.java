package com.etraveli.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BinlistResponse {
    private Country country;

    @Data
    public static class Country {
        private String alpha2;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "Response containing the country code and the associated clearing cost.")
    public static class BinlistResponseWithCost {
        @Schema(description = "ISO country code of the card issuer", example = "US")
        private String country;
        @Schema(description = "Clearing cost in dollars", example = "5.00")
        private BigDecimal cost;
    }
}
