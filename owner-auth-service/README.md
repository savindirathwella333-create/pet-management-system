# Owner & Auth Service
**Owned by:** Student 1 — Gateway Lead
**Port:** 8081

## What this service does
This is the **auth root** of the Pet Management System. It:
1. Registers pet owners and stores their (hashed) credentials.
2. Logs owners in and issues a **JWT** — a simplified, coursework-scoped
   stand-in for a full OAuth2 Authorization Server. The **API Gateway**
   (in your other zip, `api-gateway/`) validates this token on every
   subsequent request using the same shared secret.
3. Exposes owner profile lookups that **pet-service** calls internally to
   confirm an `ownerId` is real before attaching a pet to it — this is the
   interconnection between your service and Student 2's.

## Endpoints
| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | API Key | Register a new owner |
| POST | `/api/auth/login` | API Key | Login, returns `{ token, ownerId, name, email }` |
| GET | `/api/owners/{id}` | API Key | Get one owner (used by pet-service) |
| GET | `/api/owners` | API Key | List all owners |

## API Key
Every `/api/**` request **must** include:
```
X-API-KEY: owner-service-key-2026
```
(configurable via the `OWNER_SERVICE_API_KEY` env var — see `application.yml`).
Requests without it get `401 Unauthorized`. In the full system, the **Gateway**
attaches this header automatically, so end users/clients never see it.

## Run it standalone
```bash
mvn spring-boot:run
```
Swagger UI: http://localhost:8081/swagger-ui.html
H2 console: http://localhost:8081/h2-console (JDBC URL: `jdbc:h2:mem:ownerdb`)

## Run it with Docker
```bash
docker build -t owner-auth-service .
docker run -p 8081:8081 owner-auth-service
```

## Test credentials / sample requests
```bash
# Register
curl -X POST http://localhost:8081/api/auth/register \
  -H "X-API-KEY: owner-service-key-2026" -H "Content-Type: application/json" \
  -d '{"name":"Nadeesha Perera","email":"nadeesha@example.com","password":"pass123","phone":"0771234567"}'

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "X-API-KEY: owner-service-key-2026" -H "Content-Type: application/json" \
  -d '{"email":"nadeesha@example.com","password":"pass123"}'
```

## Where this fits in the whole system
```
React Client -> API Gateway (OAuth2/JWT check + CORS + Rate Limit) -> [this service]
                                                                    -> pet-service
                                                                    -> appointment-service
                                                                    -> medical-record-service
```
This zip also contains the `api-gateway/` folder — as Gateway Lead you own both.
See `../api-gateway/README.md` for the gateway setup, and the root project
`README.md` (shared file) for how all 4 members' folders + the client-app
combine into one repository with one `docker-compose.yml`.
