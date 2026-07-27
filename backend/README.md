# Ticketing Backend

A REST API for managing incident/support tickets: CRUD, input validation,
rate limiting, layered architecture (controller/service/repository).

## Stack
* Java 21, Spring Boot (Web, Data JPA, Validation, Actuator)
* PostgreSQL
* Bucket4j (in-memory, per-IP rate limiting)
* JUnit 5, Mockito, MockMvc (unit + integration tests)

## Architecture
* `model` - JPA entities and data class definitions
* `dto` - request/response mapping
* `repository` - database driver/controller
* `service` - business logic and entity/DTO mapping boundary
* `controller` - REST controller with mappings
* `exception` - global exception handling
* `config` - rate limiter and CORS setup

The service layer works with entities; the controller layer is responsible for mapping entities to response DTOs. Request DTOs only expose fields a client should be able to set at that point in the ticket's lifecycle (for example `status` cannot be set on creation - it's always server assigned to `SUBMITTED`)

## Running locally

From the `backend/` directory:

```bash
./mvnw spring-boot:run
```

This expects a PostgreSQL instance reachable via the settings below (defaults
assume `localhost:5432`). See the repo root `docker-compose.yml` to run
Postgres, the backend, and the frontend together.

## Configuration
| Variable                     | Default (local)                                 |
|-------------------------------|--------------------------------------------------|
| `SPRING_JDBC_URL`             | `jdbc:postgresql://localhost:5432/ticketing_db`   |
| `SPRING_DS_USERNAME`          | `ticketing_user`                                  |
| `SPRING_DS_PASSWORD`          | `ticketing_password`                              |
| `APP_CORS_ALLOWED_ORIGINS`    | `http://localhost:3000`                           |

In the root `docker-compose.yml`, the datasource variables are overridden so
the app connects to the `postgres` service by its service name rather than
`localhost`. `APP_CORS_ALLOWED_ORIGINS` controls which origin(s) the frontend
is served from are allowed to call the API (comma-separated for multiple).

## Health checks

Spring Boot Actuator is enabled with Kubernetes-style liveness/readiness
probe groups:
* `GET /actuator/health/liveness`
* `GET /actuator/health/readiness` (reflects database connectivity)

Both are excluded from rate limiting.

## API Reference

| Method | Endpoint                  | Description                  |
|--------|----------------------------|-------------------------------|
| POST   | `/tickets`                 | Create a new ticket           |
| GET    | `/tickets`                 | List all tickets              |
| GET    | `/tickets/{id}`             | Get a ticket by ID            |
| PATCH  | `/tickets/{id}/status`      | Update a ticket's status      |
| DELETE | `/tickets/{id}`             | Delete a ticket               |

### Example requests

Create a ticket:

```bash
curl -X POST http://localhost:8808/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Database connection pool exhausted",
    "description": "Connections maxed out on prod DB, app returning 500s",
    "priority": "CRITICAL"
  }'
```

List all tickets:

```bash
curl http://localhost:8808/tickets
```

Get a single ticket:

```bash
curl http://localhost:8808/tickets/1
```

Update status:

```bash
curl -X PATCH http://localhost:8808/tickets/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "RESOLVED"}'
```

Delete a ticket:

```bash
curl -X DELETE http://localhost:8808/tickets/1
```

## Validation

- `title` - required, non-blank, max length enforced
- `description` - optional, max length enforced
- `priority` - required (`LOW` / `MEDIUM` / `HIGH` / `CRITICAL`)
- `status` (on the status-update endpoint) - required (`SUBMITTED` / `IN_REVIEW` / `RESOLVED`)

Invalid requests return `400 Bad Request` with a per-field breakdown of what
failed, handled centrally by `GlobalExceptionHandler`. Requests for a
non-existent ticket ID return `404 Not Found`.

## Rate limiting

Each client IP is limited via an in-memory token bucket (Bucket4j), implemented
as a servlet filter that runs before requests reach any controller. Requests
exceeding the limit receive `429 Too Many Requests`. This is appropriate for a
single-instance project; a multi-instance production deployment would need a
shared store (e.g. Redis) instead of an in-memory map, since each instance
would otherwise track its own separate limits.

## Notes on design decisions

- IDs are server-generated (`GenerationType.IDENTITY`); clients never supply an
  ID when creating a ticket, which also rules out `PUT` for creation (`PUT`
  requires the client to know the target URI in advance).
- Response DTOs exist to control exactly what a client sees, independent of
  what the entity happens to contain - this matters as the entity grows over
  time with fields that aren't meant to be public.
- `userId` is reserved on the entity for future authentication work but is not
  yet wired into any endpoint, since there is no authentication mechanism in
  place to reliably populate it.
- CORS is wide open by default in local dev (`http://localhost:3000`), but
  configurable via `APP_CORS_ALLOWED_ORIGINS` so it can be locked down per
  environment.
