package com.petmgmt.appointmentservice.client;

import com.petmgmt.appointmentservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Interconnection point: appointment-service -> pet-service.
 * Before booking an appointment, confirm the given petId actually exists by
 * calling pet-service's GET /api/pets/{id} directly (bypassing the Gateway),
 * carrying pet-service's own API key.
 */
@Component
public class PetClient {

    private final RestTemplate restTemplate;

    @Value("${pet-service.base-url}")
    private String petServiceBaseUrl;

    @Value("${pet-service.api-key}")
    private String petServiceApiKey;

    public PetClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean petExists(Long petId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", petServiceApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    petServiceBaseUrl + "/api/pets/" + petId,
                    HttpMethod.GET,
                    entity,
                    String.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new BadRequestException("Could not verify pet (pet-service unreachable): " + e.getMessage());
        }
    }
}
