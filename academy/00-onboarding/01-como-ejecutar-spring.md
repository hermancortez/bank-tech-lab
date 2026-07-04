# Cómo ejecutar Spring Boot

## ¿Qué es?

Spring Boot inicia el contexto, configura beans y levanta el servidor embebido desde la clase `@SpringBootApplication`.

## ¿Por qué importa en banca?

Una ejecución reproducible reduce errores de ambiente y acelera diagnóstico e incorporación de personas.

## ¿Cuándo usarlo?

Durante desarrollo local y pruebas de integración del servicio.

## ¿Cuándo NO usarlo?

No ejecutes el wrapper desde la raíz: allí no existe. En ambientes productivos se ejecuta el artefacto o contenedor construido por CI.

## Ejemplo práctico

```bash
docker compose -f infra/docker/docker-compose.yml up -d
cd backend-java/customer-service
./mvnw spring-boot:run
```

## Preguntas típicas de entrevista

1. ¿Qué problema resuelve y qué alternativa existe?
2. ¿Qué fallo esperarías en producción y cómo lo observarías?
3. ¿Qué trade-off aceptarías en un sistema bancario?

## Resumen rápido

Wrapper dentro del servicio, dependencias locales activas y health check en `/actuator/health`.
