# Medical Record Service
**Owned by:** Student 4
**Port:** 8084

## What this service does
Manages medical/vaccination records for pets. Before adding a record, this
service calls **appointment-service** directly to confirm the given
`appointmentId` actually exists — completing the full interconnection chain
across the team:

```
owner-auth-service -> pet-service -> appointment-service -> medical-record-service
   (Student 1)         (Student 2)      (Student 3)            (Student 4)
```

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | `/api/records` | Add a record (validates `appointmentId` against appointment-service) |
| GET | `/api/records/{id}` | Get a record by id |
| GET | `/api/records/pet/{petId}` | List all records for a pet |
| GET | `/api/records` | List all records |
| DELETE | `/api/records/{id}` | Remove a record |

All routes require `X-API-KEY: medical-record-service-key-2026` (the Gateway
attaches this automatically for you).

## Run it standalone
Start **appointment-service** first (port 8083), then:
```bash
mvn spring-boot:run
```
Swagger UI: http://localhost:8084/swagger-ui.html

## Run it with Docker
```bash
docker build -t medical-record-service .
docker run -p 8084:8084 -e APPOINTMENT_SERVICE_URL=http://host.docker.internal:8083 medical-record-service
```

## Sample requests
```bash
curl -X POST http://localhost:8084/api/records \
  -H "X-API-KEY: medical-record-service-key-2026" -H "Content-Type: application/json" \
  -d '{"petId":1,"appointmentId":1,"diagnosis":"Healthy, routine checkup","treatment":"Rabies booster administered"}'

curl http://localhost:8084/api/records/pet/1 -H "X-API-KEY: medical-record-service-key-2026"
```
