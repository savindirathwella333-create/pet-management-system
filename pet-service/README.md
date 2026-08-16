# Pet Service
**Owned by:** Student 2
**Port:** 8082

## What this service does
Manages pet profiles. Each pet belongs to an owner (`ownerId`). This is the
**interconnection point**: before creating a pet, this service calls
**owner-auth-service** directly to confirm the owner actually exists.
Later, **appointment-service** (Student 3) will call this service the same
way to confirm a `petId` is real before booking a vet visit.

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | `/api/pets` | Create a pet (validates `ownerId` against owner-auth-service) |
| GET | `/api/pets/{id}` | Get a pet by id |
| GET | `/api/pets/owner/{ownerId}` | List all pets for an owner |
| GET | `/api/pets` | List all pets |
| PUT | `/api/pets/{id}` | Update a pet |
| DELETE | `/api/pets/{id}` | Remove a pet |

All routes require `X-API-KEY: pet-service-key-2026` (the Gateway attaches
this automatically for you).

## Run it standalone
Start **owner-auth-service** first (port 8081), then:
```bash
mvn spring-boot:run
```
Swagger UI: http://localhost:8082/swagger-ui.html

If you don't have owner-auth-service running yet, pet creation will fail
with a 400 saying the owner service is unreachable — that's expected and
demonstrates the interconnection is real, not mocked.

## Run it with Docker
```bash
docker build -t pet-service .
docker run -p 8082:8082 -e OWNER_SERVICE_URL=http://host.docker.internal:8081 pet-service
```

## Sample requests
```bash
# First register + note an ownerId from owner-auth-service (port 8081), then:
curl -X POST http://localhost:8082/api/pets \
  -H "X-API-KEY: pet-service-key-2026" -H "Content-Type: application/json" \
  -d '{"name":"Simba","species":"Cat","breed":"Persian","age":2,"ownerId":1}'

curl http://localhost:8082/api/pets/owner/1 -H "X-API-KEY: pet-service-key-2026"
```
