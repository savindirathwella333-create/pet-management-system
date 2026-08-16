# Pet Management System — Microservices Project
**Module:** Service-Oriented Computing · **Deliverable:** GitHub Repository & Project Report

## 1. What this is
A distributed microservices system for a Pet Management System, built to
satisfy the coursework brief: 4 students each own one Spring Boot
microservice, all fronted by a single API Gateway (OAuth2-style auth + CORS
+ rate limiting) and one React client, fully containerized with Docker.

Scope note: business logic inside each service is intentionally minimal
(one core function per service, in-memory H2 database, no edge cases beyond
what's needed to demonstrate the pattern) — the point of the project is the
**architecture and the interconnections**, not building a production pet
management SaaS.

## 2. Team & work breakdown
| Student | Role | Microservice | Port | Key Endpoints |
|---|---|---|---|---|
| Student 1 | Gateway Lead | Owner & Auth Service + API Gateway | 8081 / 8080 | `/api/auth/register`, `/api/auth/login`, `/api/owners/{id}` |
| Student 2 | Member | Pet Service | 8082 | `/api/pets`, `/api/pets/{id}`, `/api/pets/owner/{id}` |
| Student 3 | Member | Appointment Service | 8083 | `/api/appointments`, `/api/appointments/pet/{id}` |
| Student 4 | Member | Medical Record Service | 8084 | `/api/records`, `/api/records/pet/{id}` |
| Shared | — | React Client | 3000 | Consumes everything through the Gateway |

## 3. Architecture
```
                         ┌────────────────────┐
   Browser (React) ───▶  │   API Gateway :8080 │
                         │  OAuth2/JWT · CORS   │
                         │  · Rate Limiting     │
                         └──────────┬──────────┘
              attaches X-API-KEY per route, forwards to:
        ┌───────────────┬──────────┴──────────┬──────────────────┐
        ▼               ▼                     ▼                  ▼
 owner-auth-service  pet-service       appointment-service  medical-record-service
      :8081             :8082                :8083                :8084
        ▲                  │                     │                    │
        └── validates ownerId    validates petId ──┘   validates appointmentId ┘
             (direct call)         (direct call)          (direct call)
```
Each arrow between services is a **direct, synchronous REST call carrying
that target service's own API key** — this is the "4 functions that are
interconnected": owner → pet → appointment → medical record, each step
validated by calling the previous service for real.

## 4. Assembling the one group repository
Each member has their own zip. Create **one** shared GitHub repo with this
layout, then commit each folder under the matching member's own commits (for
the "clear commit history reflecting individual contributions" requirement):

```
pet-management-system/
├── owner-auth-service/       ← from Student 1's zip
├── api-gateway/               ← from Student 1's zip
├── pet-service/                ← from Student 2's zip
├── appointment-service/        ← from Student 3's zip
├── medical-record-service/     ← from Student 4's zip
├── client-app/                 ← shared React client (separate zip)
└── docker-compose.yml          ← this file (shared)
```
Suggested flow: one member creates the repo and pushes this root
`README.md` + `docker-compose.yml` first, then each student clones it,
adds their own folder, and pushes on their own branch/PRs so their commits
are individually attributed.

## 5. Running everything
```bash
docker compose up --build
```
Then open:
- React client: http://localhost:3000
- API Gateway: http://localhost:8080
- Swagger UI — owner-auth-service: http://localhost:8081/swagger-ui.html
- Swagger UI — pet-service: http://localhost:8082/swagger-ui.html
- Swagger UI — appointment-service: http://localhost:8083/swagger-ui.html
- Swagger UI — medical-record-service: http://localhost:8084/swagger-ui.html

## 6. API keys & test credentials (for the README's required table)
| Service | Header | Test value |
|---|---|---|
| owner-auth-service | `X-API-KEY` | `owner-service-key-2026` |
| pet-service | `X-API-KEY` | `pet-service-key-2026` |
| appointment-service | `X-API-KEY` | `appointment-service-key-2026` |
| medical-record-service | `X-API-KEY` | `medical-record-service-key-2026` |

The client and Postman never need these — the Gateway attaches them. They're
only needed for testing a microservice directly (e.g. via Swagger UI or curl).

## 7. What each report section maps to
- **System Architecture & Design** → section 3 diagram above + each service's README.
- **Service Breakdown** → each microservice's own README (endpoints, models, how validation works).
- **Security & Infrastructure** → `api-gateway/README.md` (OAuth2/JWT, rate limiting, CORS) + each service's `ApiKeyFilter`.
- **Client Integration** → run the 4-step flow in `client-app` (register → add pet → book appointment → add record) and screenshot each step.
- **Individual Contribution Matrix** → section 2 table above, filled in with real names.

## 8. Extending beyond this scope
This is deliberately a minimal-but-real version. Natural next steps if a
team wants to go further for extra credit: swap H2 for a real Postgres per
service, replace the simplified JWT flow with Spring Authorization Server,
move the rate limiter to Redis, add integration tests, and add pagination to
list endpoints.
