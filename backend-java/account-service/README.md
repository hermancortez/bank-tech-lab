# account-service

Microservicio Spring Boot basado en Arquitectura Hexagonal.

## Puerto

```text
8082
```

## API

```text
POST /accounts
GET /accounts
GET /accounts/{id}
GET /accounts/customer/{customerId}
POST /accounts/{id}/deposit
POST /accounts/{id}/withdraw
POST /accounts/{id}/block
POST /accounts/{id}/close
```

## Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Kafka
- Redis
- OpenAPI
- Actuator
- Docker
- Kubernetes

## Arquitectura

```text
domain/
application/
infrastructure/
config/
shared/
```
