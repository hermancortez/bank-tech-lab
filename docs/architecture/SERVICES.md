# Services Catalog

## Objetivo

Documentar los servicios principales de Bank Tech Lab.

---

## Microservicios backend

| Servicio | Descripción | Tecnología | Estado |
|----------|-------------|------------|--------|
| api-gateway | Entrada principal a la plataforma | Spring Cloud Gateway | Pendiente |
| customer-service | Gestión de clientes bancarios | Spring Boot | En construcción |
| account-service | Gestión de cuentas bancarias | Spring Boot | Pendiente |
| auth-service | Autenticación y autorización | Spring Security / OAuth2 | Pendiente |
| payment-service | Gestión de pagos | Spring Boot | Pendiente |
| transfer-service | Transferencias entre cuentas | Spring Boot | Pendiente |
| notification-service | Notificaciones transaccionales | Spring Boot | Pendiente |
| document-service | Gestión documental | Spring Boot | Pendiente |
| audit-service | Auditoría técnica y funcional | Spring Boot | Pendiente |

---

## Servicios de infraestructura

| Servicio | Descripción |
|----------|-------------|
| PostgreSQL | Base de datos relacional principal |
| Redis | Caché distribuida |
| Kafka | Mensajería asíncrona |
| RabbitMQ | Mensajería alternativa para comparación |
| Prometheus | Recolección de métricas |
| Grafana | Visualización de métricas |
| Jaeger | Trazabilidad distribuida |