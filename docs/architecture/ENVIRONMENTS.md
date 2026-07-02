# Environments

## Objetivo

Definir los ambientes utilizados por Bank Tech Lab.

---

## Local

Ambiente de desarrollo personal.

- Docker Compose
- PostgreSQL local
- Kafka local
- Redis local
- Servicios Spring Boot ejecutados desde la terminal o IDE

---

## DEV

Ambiente simulado de desarrollo.

- Kubernetes local con kind
- Manifiestos Kubernetes
- ConfigMaps
- Secrets
- Ingress

---

## QA

Ambiente simulado para validación.

- Despliegue con Helm
- Tests automatizados
- Pruebas de integración
- Pruebas de contrato

---

## PRD

Ambiente conceptual productivo.

- Kubernetes administrado
- API Gateway
- Observabilidad
- Seguridad
- Alta disponibilidad
- Autoscaling