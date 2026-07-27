# Ticketing App

A full-stack incident/support ticketing app, structured as a monorepo:

* [`backend/`](backend/README.md) - Java 21 / Spring Boot REST API
* [`frontend/`](frontend/README.md) - Next.js (TypeScript) UI

CI/CD and Kubernetes/infrastructure manifests are being built out separately.

## Quick start - Docker Compose

Runs Postgres, the backend, and the frontend together, built from source:

```bash
docker-compose up --build
```

* Frontend: `http://localhost:3000`
* Backend API: `http://localhost:8808`
* Postgres is also exposed on the host at port `9876` (e.g. for DBeaver/pgAdmin)

## Quick start - prebuilt backend image

Pull the backend image published on each [release](https://github.com/lukasdo-git/ticketing-api/releases)
and run it against a local Postgres, no build required:

```bash
curl -O https://raw.githubusercontent.com/lukasdo-git/ticketing-api/main/docker-compose.release.yml
docker-compose -f docker-compose.release.yml up
```

The API will be available at `http://localhost:8808`. This currently only
covers the backend - it predates the frontend/monorepo split and will need
updating once the CI/CD pipeline builds and publishes a frontend image too.

## Stack

* **Backend**: Java 21, Spring Boot (Web, Data JPA, Validation, Actuator), PostgreSQL, Bucket4j rate limiting, JUnit 5/Mockito/MockMvc
* **Frontend**: Next.js 16 (App Router), TypeScript, Tailwind CSS
* **Containerization**: Docker / Docker Compose for local dev

See [`backend/README.md`](backend/README.md) and [`frontend/README.md`](frontend/README.md)
for details specific to each app (API reference, architecture, configuration).
