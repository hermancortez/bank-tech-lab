# Port Allocation

## Objetivo

Definir la asignación oficial de puertos utilizados por Bank Tech Lab.

Esta convención evita conflictos entre microservicios, herramientas de infraestructura y aplicaciones de observabilidad.

---

# Backend

| Servicio | Puerto | Tecnología |
|----------|--------:|------------|
| API Gateway | 8080 | Spring Cloud Gateway |
| Customer Service | 8081 | Spring Boot |
| Account Service | 8082 | Spring Boot |
| Auth Service | 8083 | Spring Boot |
| Payment Service | 8084 | Spring Boot |
| Transfer Service | 8085 | Spring Boot |
| Notification Service | 8086 | Spring Boot |
| Document Service | 8087 | Spring Boot |
| Audit Service | 8088 | Spring Boot |

---

# Frontend

| Aplicación | Puerto |
|------------|--------:|
| Angular | 4200 |

---

# Bases de datos

| Servicio | Puerto |
|----------|--------:|
| PostgreSQL | 5432 |
| Redis | 6379 |
| MongoDB | 27017 |

---

# Mensajería

| Servicio | Puerto |
|----------|--------:|
| Kafka | 9092 |
| Kafka UI | 8090 |
| RabbitMQ | 5672 |
| RabbitMQ Management | 15672 |

---

# Observabilidad

| Servicio | Puerto |
|----------|--------:|
| Prometheus | 9090 |
| Grafana | 3000 |
| Jaeger | 16686 |

---

# Kubernetes

| Servicio | Puerto |
|----------|--------:|
| Kubernetes API | 6443 |
| ArgoCD | 32000 |
| Istio Gateway | 80 / 443 |

---

# Convenciones

- Nunca reutilizar un puerto.
- Todos los microservicios deben exponerse mediante API Gateway.
- Ningún frontend consume directamente un microservicio.
- Los puertos deben mantenerse consistentes entre Desarrollo, QA y Laboratorio.

---

# Próximos puertos disponibles

8091
8092
8093
8094
8095
