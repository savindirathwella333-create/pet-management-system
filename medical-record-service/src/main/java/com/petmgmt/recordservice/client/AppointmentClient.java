package com.petmgmt.recordservice.client;

import com.petmgmt.recordservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Interconnection point: medical-record-service -> appointment-service.
 * Before adding a medical record, confirm the given appointmentId actually
 * exists by calling appointment-service's GET /api/appointments/{id}
 * directly, carrying appointment-service's own API key. This completes the
 * chain: owner -> pet -> appointment -> medical record.
 */
@Component
public class AppointmentClient {

    private final RestTemplate restTemplate;

    @Value("${appointment-service.base-url}")
    private String appointmentServiceBaseUrl;

    @Value("${appointment-service.api-key}")
    private String appointmentServiceApiKey;

    public AppointmentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean appointmentExists(Long appointmentId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", appointmentServiceApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    appointmentServiceBaseUrl + "/api/appointments/" + appointmentId,
                    HttpMethod.GET,
                    entity,
                    String.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new BadRequestException("Could not verify appointment (appointment-service unreachable): " + e.getMessage());
        }
    }
}
