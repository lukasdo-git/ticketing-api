# Incident Ticketing API
A REST API for managing incident/support tickets, built as a portfolio project to practice backend fundamentals: CRUD, input validation, rate limiting, layered architecture (controller/service/repository), and containerized deployment.

## Stack
* Java 21, Spring Boot (Web, Data JPA, Validation)
* PostgreSQL
* Bucket4j (in-memory, per-IP rate limiting)
* JUnit 5, Mockito, MockMvc (unit + integration tests)
* Docker / Docker Compose

## Architecture
* `model` - JPA entities and data class definitions
* `dto` - request/response mapping
* `repository` - database driver/controller
* `service` - business logic and entity/DTO mapping boundary
* `controller` - REST controller with mappings
* `exception` - global exception handling
* `config` - rate limiter setup

The service layer works with entities; the controller layer is responsible for mapping entities to response DTOs. Request DTOs only expose fields a client should be able to set at that point in the ticket's lifecycle (for example `status` cannot be set on creation - it's always server assigned to `SUBMITTED`)

## Running locally with Docker
```bash
docker-compose up --build
```
This starts PostgreSQL, and after making sure the DB started correctly starts the API.
The API will be available at:
```plaintext
http://localhost:8808
```
PostgreSQL is exposed on the host at port `9876` in case you want to connect with a client such as DBeaver or pgAdmin.

## Configuration
| Variable              | Default (local)                                   |
|------------------------|----------------------------------------------------|
| `SPRING_JDBC_URL`      | `jdbc:postgresql://localhost:5432/ticketing_db`     |
| `SPRING_DS_USERNAME`   | `ticketing_user`                                    |
| `SPRING_DS_PASSWORD`   | `ticketing_password`

In `docker-compose.yml`, these are overridden so the app connects to the `postgres` service by its service name rather than `localhost`.

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
