package com.petmgmt.recordservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Medical Record Service
 * Owned by: Student 4
 *
 * Responsibilities:
 *  - Manage medical/vaccination records for a pet
 *  - Validates that a given appointmentId is real by calling appointment-service
 *    (interconnection with Student 3's service) - completing the chain:
 *    owner -> pet -> appointment -> medical record
 */
@SpringBootApplication
public class MedicalRecordServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
