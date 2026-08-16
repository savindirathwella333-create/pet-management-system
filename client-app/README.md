# Pet Management System — React Client
**Tech:** React 18 + Vite + React Router
**Port (dev):** 3000 · **Port (docker/nginx):** 80

## What this is
The single unified frontend required by the brief — it talks **only** to the
API Gateway (`http://localhost:8080`), never directly to a microservice, and
never handles any `X-API-KEY` (the Gateway attaches those).

It walks the full interconnection chain end-to-end:
```
Login/Register (owner-auth-service)
  → Dashboard: your pets (pet-service)
    → Pet detail: book/view appointments (appointment-service)
      → Appointment detail: add/view medical records (medical-record-service)
```
Every data card is visually "stamped" with the microservice + port that
actually owns that data, so screenshots for the report clearly show which
teammate's service is being exercised.

## Setup
```bash
npm install
cp .env.example .env   # adjust VITE_API_BASE_URL if the gateway isn't on :8080
npm run dev
```
Open http://localhost:3000 — make sure the API Gateway (and the 4
microservices behind it) are running first.

## Build & run with Docker
```bash
docker build -t pet-client-app --build-arg VITE_API_BASE_URL=http://localhost:8080 .
docker run -p 3000:80 pet-client-app
```

## Pages
| Route | Purpose |
|---|---|
| `/login`, `/register` | Owner auth (calls owner-auth-service via gateway) |
| `/` | Dashboard — list & add pets |
| `/pets/:petId` | Pet detail — list & book appointments |
| `/appointments/:appointmentId` | Appointment detail — update status, list & add medical records |

## Screenshots for the report
The brief's "Client Integration" section asks for screenshots showing the
unified client interacting with all backend services — the 4-step flow above
(register → add pet → book appointment → add record) is designed to give you
exactly that in one pass.
