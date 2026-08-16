package com.petmgmt.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway
 * Owned by: Student 1 (Gateway Lead)
 *
 * Single entry point for the whole system. Responsibilities per the brief:
 *  1. OAuth2-style authentication: validates JWTs issued by owner-auth-service
 *     (see filter/JwtAuthenticationFilter).
 *  2. CORS: allows the React client's origin (see config/CorsConfig).
 *  3. Rate limiting: throttles requests per client IP (see filter/RateLimitingFilter).
 *  4. Routing: forwards to owner-auth-service, pet-service, appointment-service,
 *     medical-record-service, attaching each service's required X-API-KEY
 *     (see application.yml routes) so the client never needs to know it.
 */
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
