package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.BinlistDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for interacting with the Binlist API to retrieve card information.
 * This class is annotated with {@code @Service}, indicating that it is a Spring service component.
 * It is responsible for making HTTP requests to the Binlist API to obtain information about a given card number,
 * such as the country code associated with it.
 * It uses {@link RestTemplate} for making HTTP GET requests to external services.
 * This class provides methods that can be used in other service or controller classes to retrieve information
 * related to card numbers.
 *
 * @see org.springframework.stereotype.Service
 * @see org.springframework.web.client.RestTemplate
 */

@Service
public class BinlistService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BinlistDto getCountryCodeByCardNumber(String cardNumber) {
        String url = "https://lookup.binlist.net/" + cardNumber;
        ResponseEntity<BinlistDto> response = restTemplate.getForEntity(url, BinlistDto.class);
        return response.getBody();
    }
}

