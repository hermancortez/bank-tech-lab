#!/usr/bin/env bash
set -e

SERVICE_NAME=$1
BASE_PACKAGE=$2

if [ -z "$SERVICE_NAME" ] || [ -z "$BASE_PACKAGE" ]; then
  echo "Uso:"
  echo "./scripts/create-spring-service.sh <service-name> <base-package>"
  echo ""
  echo "Ejemplo:"
  echo "./scripts/create-spring-service.sh customer-service cl.banktech.customer"
  exit 1
fi

SERVICE_DIR="backend-java/$SERVICE_NAME"
PACKAGE_DIR=$(echo "$BASE_PACKAGE" | tr '.' '/')

mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR"/{config,shared}
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/domain/model"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/domain/port/in"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/domain/port/out"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/application/usecase"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/infrastructure/rest"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/infrastructure/persistence"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/infrastructure/messaging"
mkdir -p "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/infrastructure/config"
mkdir -p "$SERVICE_DIR/src/test/java/$PACKAGE_DIR"

cat > "$SERVICE_DIR/README.md" <<EOF
# $SERVICE_NAME

Microservicio Spring Boot basado en Arquitectura Hexagonal.

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

\`\`\`text
domain/
application/
infrastructure/
config/
shared/
\`\`\`
EOF

cat > "$SERVICE_DIR/Dockerfile" <<EOF
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > "$SERVICE_DIR/.env.example" <<EOF
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=5432
DB_NAME=bankdb
DB_USER=bankuser
DB_PASSWORD=bankpass
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
REDIS_HOST=localhost
REDIS_PORT=6379
EOF

cat > "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/package-info.java" <<EOF
/**
 * $SERVICE_NAME base package.
 */
package $BASE_PACKAGE;
EOF

echo "Servicio $SERVICE_NAME inicializado con estructura hexagonal."