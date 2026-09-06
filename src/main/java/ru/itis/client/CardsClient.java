package ru.itis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itis.dto.response.client.CardResponse;

@Component
@RequiredArgsConstructor
public class CardsClient {

    private final RestTemplate restTemplate;

    @Value("${cards-service.url}")
    private String baseUrl;

    @Cacheable(value = "cards", key = "#contractName")
    public CardResponse getCardByContractName(String contractName) {
        String url = String.format("%s/by-contract/%s", baseUrl, contractName);
        return restTemplate.getForObject(url, CardResponse.class);
    }

}
