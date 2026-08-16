package com.petmgmt.petservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Pet Service
 * Owned by: Student 2
 *
 * Responsibilities:
 *  - CRUD for pet profiles
 *  - Validates that a pet's ownerId is a real registered owner by calling
 *    owner-auth-service (this is the interconnection with Student 1's service)
 *  - Its own pets are, in turn, validated by appointment-service (Student 3)
 */
@SpringBootApplication
public class PetServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
