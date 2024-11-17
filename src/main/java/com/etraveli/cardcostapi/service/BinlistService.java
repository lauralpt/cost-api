package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BinlistService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BinlistResponse getCountryCodeByCardNumber(String cardNumber) {
        String url = "https://lookup.binlist.net/" + cardNumber;
        ResponseEntity<BinlistResponse> response = restTemplate.getForEntity(url, BinlistResponse.class);
        return response.getBody();
    }
}

