package com.petmgmt.ownerservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Enforces API Key verification on every /api/** endpoint of this microservice,
 * per the assignment brief ("every microservice must enforce API Key verification
 * so that endpoints reject unauthenticated or direct unauthorized calls").
 *
 * In this system the API Gateway is the trusted caller and attaches this header
 * automatically on every proxied request. Direct calls to this service that skip
 * the Gateway (and therefore don't carry the key) are rejected with 401.
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Value("${api.key}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String providedKey = request.getHeader(API_KEY_HEADER);

        if (providedKey == null || !providedKey.equals(expectedApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Missing or invalid X-API-KEY header\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
