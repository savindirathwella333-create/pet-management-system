# Appointment Service
**Owned by:** Student 3
**Port:** 8083

## What this service does
Manages vet appointment bookings for pets. Before creating an appointment,
this service calls **pet-service** directly to confirm the given `petId`
actually exists — the interconnection with Student 2's service. Later,
**medical-record-service** (Student 4) will call this service the same way
to confirm an `appointmentId` before attaching a medical record to it.

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | `/api/appointments` | Book an appointment (validates `petId` against pet-service) |
| GET | `/api/appointments/{id}` | Get an appointment by id |
| GET | `/api/appointments/pet/{petId}` | List all appointments for a pet |
| GET | `/api/appointments` | List all appointments |
| PUT | `/api/appointments/{id}/status` | Update status: `SCHEDULED` / `COMPLETED` / `CANCELLED` |

All routes require `X-API-KEY: appointment-service-key-2026` (the Gateway
attaches this automatically for you).

## Run it standalone
Start **pet-service** first (port 8082), then:
```bash
mvn spring-boot:run
```
Swagger UI: http://localhost:8083/swagger-ui.html

## Run it with Docker
```bash
docker build -t appointment-service .
docker run -p 8083:8083 -e PET_SERVICE_URL=http://host.docker.internal:8082 appointment-service
```

## Sample requests
```bash
curl -X POST http://localhost:8083/api/appointments \
  -H "X-API-KEY: appointment-service-key-2026" -H "Content-Type: application/json" \
  -d '{"petId":1,"vetName":"Dr. Silva","appointmentDate":"2026-08-20T10:00:00","reason":"Annual checkup"}'

curl -X PUT http://localhost:8083/api/appointments/1/status \
  -H "X-API-KEY: appointment-service-key-2026" -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'
```
