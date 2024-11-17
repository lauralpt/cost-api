package com.etraveli.cardcostapi.dto;

import lombok.Data;

@Data
public class BinlistResponse {
    private Country country;

    @Data
    public static class Country {
        private String alpha2;
    }
}
