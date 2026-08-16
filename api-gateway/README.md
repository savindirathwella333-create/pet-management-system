# API Gateway
**Owned by:** Student 1 — Gateway Lead
**Port:** 8080 (this is the ONLY port the React client talks to)

## What this service does
Single entry point for the whole system, satisfying the brief's "API Gateway &
Security Infrastructure" requirements:

| Requirement | How it's implemented |
|---|---|
| OAuth2 Authentication/Authorization | `filter/JwtAuthenticationFilter` validates a JWT (`Authorization: Bearer <token>`) issued by owner-auth-service's `/api/auth/login`, on every route except `/api/auth/register` and `/api/auth/login`. Simplified vs. a full Authorization Server — appropriate for this coursework's scope. |
| CORS | `config/CorsConfig` allows the React client's origin (`localhost:3000` / `5173`) with credentials. |
| Rate Limiting | `filter/RateLimitingFilter` — in-memory, 30 requests/minute per client IP. |
| Routing | `application.yml` routes each `/api/**` prefix to the right microservice, and **attaches that service's `X-API-KEY` automatically** so neither the browser nor the end user ever needs to know it. |

## How a request flows
```
Browser --(Bearer JWT)--> Gateway:8080 --(+X-API-KEY)--> owner-auth-service:8081
                                        --(+X-API-KEY)--> pet-service:8082
                                        --(+X-API-KEY)--> appointment-service:8083
                                        --(+X-API-KEY)--> medical-record-service:8084
```

## Run it standalone
Make sure the 4 microservices are running first (default localhost ports),
then:
```bash
mvn spring-boot:run
```
Gateway routes are visible at: http://localhost:8080/actuator/gateway/routes

## Run it with Docker
```bash
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```
When run via the root `docker-compose.yml`, service URLs are wired
automatically via env vars (`OWNER_SERVICE_URL`, `PET_SERVICE_URL`, etc.) —
see the shared `docker-compose.yml` in the project root files.

## Try it end-to-end
```bash
# 1. Login through the gateway (public route)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"nadeesha@example.com","password":"pass123"}'

# 2. Use the returned token to call a protected route
curl http://localhost:8080/api/pets/owner/1 \
  -H "Authorization: Bearer <token-from-step-1>"
```

## Note on scope
This gateway implements a simplified JWT bearer-token flow rather than a full
OAuth2 Authorization Server (e.g. Spring Authorization Server, Keycloak, or
Okta) with grant types, scopes, and refresh tokens. That's a deliberate
scope decision for a 4-person coursework project — the report should mention
this as a design decision with a note on how it maps to real OAuth2 concepts
(login = password grant, JWT = access token) and how it could be extended.
