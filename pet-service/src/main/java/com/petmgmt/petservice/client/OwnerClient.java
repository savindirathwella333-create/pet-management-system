package com.petmgmt.petservice.client;

import com.petmgmt.petservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Interconnection point: pet-service -> owner-auth-service.
 * Before a pet is created, we confirm the given ownerId actually exists by
 * calling owner-auth-service's GET /api/owners/{id}. This is a direct
 * service-to-service call (bypassing the Gateway), so it must carry
 * owner-auth-service's own API key.
 */
@Component
public class OwnerClient {

    private final RestTemplate restTemplate;

    @Value("${owner-service.base-url}")
    private String ownerServiceBaseUrl;

    @Value("${owner-service.api-key}")
    private String ownerServiceApiKey;

    public OwnerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean ownerExists(Long ownerId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", ownerServiceApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    ownerServiceBaseUrl + "/api/owners/" + ownerId,
                    HttpMethod.GET,
                    entity,
                    String.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            // owner-service unreachable — fail closed with a clear message rather
            // than silently allowing an unvalidated pet to be created
            throw new BadRequestException("Could not verify owner (owner-auth-service unreachable): " + e.getMessage());
        }
    }
}
