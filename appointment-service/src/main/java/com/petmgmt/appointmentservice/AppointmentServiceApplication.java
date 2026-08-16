package com.petmgmt.appointmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Appointment Service
 * Owned by: Student 3
 *
 * Responsibilities:
 *  - Book / manage vet appointments for a pet
 *  - Validates that a given petId is real by calling pet-service
 *    (interconnection with Student 2's service)
 *  - Its appointments are, in turn, referenced by medical-record-service (Student 4)
 */
@SpringBootApplication
public class AppointmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
