package com.petmgmt.ownerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Owner & Auth Service
 * Owned by: Student 1 (Gateway Lead)
 *
 * Responsibilities:
 *  - Register / authenticate pet owners
 *  - Issue JWTs consumed by the API Gateway (simplified OAuth2-style flow)
 *  - Expose owner profile data used by pet-service to validate ownerId references
 */
@SpringBootApplication
public class OwnerAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnerAuthServiceApplication.class, args);
    }
}
