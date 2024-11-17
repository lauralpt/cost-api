package com.etraveli.cardcostapi.dto;

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
    public static class BinlistResponseWithCost {
        private String country;
        private BigDecimal cost;
    }
}
