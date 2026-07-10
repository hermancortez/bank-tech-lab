# Bank Tech Lab

Laboratorio práctico para prepararme como Líder Técnico y Arquitecto Java en entornos bancarios.

## Objetivo

Construir una plataforma bancaria moderna aplicando:

- Java 21
- Spring Boot
- Arquitectura Hexagonal
- Microservicios
- PostgreSQL
- Kafka
- Redis
- Docker
- Kubernetes
- Observabilidad
- DevSecOps
- Gobierno tecnológico

## Módulos principales

- `academy/`: guía rápida de estudio para entrevistas técnicas.
- `backend-java/`: microservicios Java con Spring Boot.
- `infra/`: Docker, Kubernetes, Helm, Istio e Ingress.
- `observability/`: Prometheus, Grafana y OpenTelemetry.
- `docs/`: ADR, C4, Blueprints, Golden Paths y Roadmaps.

## Kubernetes local

El cluster local se opera con `kind`, `kubectl` y `k9s`.

- Guia: `docs/infrastructure/KUBERNETES-LOCAL.md`
- Contexto esperado: `kind-laboratorio`
- Abrir consola visual: `k9s --context kind-laboratorio`

## Primer objetivo técnico

Crear `customer-service` con:

- Spring Boot 3
- Java 21
- PostgreSQL
- JPA
- OpenAPI
- Actuator
- Arquitectura Hexagonal
