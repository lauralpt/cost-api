package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BinlistService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BinlistDto getCountryCodeByCardNumber(String cardNumber) {
        String url = "https://lookup.binlist.net/" + cardNumber;
        ResponseEntity<BinlistDto> response = restTemplate.getForEntity(url, BinlistDto.class);
        return response.getBody();
    }
}

