#!/usr/bin/env bash
set -e

SERVICE_NAME=$1
BASE_PACKAGE=$2
SERVICE_PORT=$3

if [ -z "$SERVICE_NAME" ] || [ -z "$BASE_PACKAGE" ] || [ -z "$SERVICE_PORT" ]; then
  echo "Uso:"
  echo "./scripts/create-spring-service.sh <service-name> <base-package> <port>"
  echo ""
  echo "Ejemplo:"
  echo "./scripts/create-spring-service.sh account-service cl.banktech.account 8082"
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
mkdir -p "$SERVICE_DIR/src/main/resources"
mkdir -p "$SERVICE_DIR/src/test/java/$PACKAGE_DIR"

mkdir -p "infra/kubernetes/$SERVICE_NAME"
mkdir -p "docs/services/$SERVICE_NAME"

cat > "$SERVICE_DIR/pom.xml" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.3</version>
    <relativePath/>
  </parent>
  <groupId>${BASE_PACKAGE%.*}</groupId>
  <artifactId>$SERVICE_NAME</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <properties><java.version>21</java.version></properties>
  <dependencies>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-actuator</artifactId></dependency>
    <dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
  </dependencies>
  <build><plugins><plugin><groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId></plugin></plugins></build>
</project>
EOF

cat > "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/Application.java" <<EOF
package $BASE_PACKAGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
EOF

cat > "$SERVICE_DIR/README.md" <<EOF
# $SERVICE_NAME

Microservicio Spring Boot basado en Arquitectura Hexagonal.

## Puerto

\`\`\`text
$SERVICE_PORT
\`\`\`

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

EXPOSE $SERVICE_PORT

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > "$SERVICE_DIR/.env.example" <<EOF
SERVER_PORT=$SERVICE_PORT
DB_HOST=localhost
DB_PORT=5432
DB_NAME=bankdb
DB_USER=bankuser
DB_PASSWORD=bankpass
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
REDIS_HOST=localhost
REDIS_PORT=6379
EOF

cat > "$SERVICE_DIR/src/main/resources/application.yml" <<EOF
server:
  port: $SERVICE_PORT

spring:
  application:
    name: $SERVICE_NAME

  datasource:
    url: jdbc:postgresql://\${DB_HOST:localhost}:\${DB_PORT:5432}/\${DB_NAME:bankdb}
    username: \${DB_USER:bankuser}
    password: \${DB_PASSWORD:bankpass}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: false

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
EOF

cat > "$SERVICE_DIR/src/main/java/$PACKAGE_DIR/package-info.java" <<EOF
/**
 * $SERVICE_NAME base package.
 */
package $BASE_PACKAGE;
EOF

cat > "infra/kubernetes/$SERVICE_NAME/deployment.yaml" <<EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $SERVICE_NAME
spec:
  replicas: 1
  selector:
    matchLabels:
      app: $SERVICE_NAME
  template:
    metadata:
      labels:
        app: $SERVICE_NAME
    spec:
      containers:
        - name: $SERVICE_NAME
          image: $SERVICE_NAME:local
          ports:
            - containerPort: $SERVICE_PORT
          env:
            - name: SERVER_PORT
              value: "$SERVICE_PORT"
EOF

cat > "infra/kubernetes/$SERVICE_NAME/service.yaml" <<EOF
apiVersion: v1
kind: Service
metadata:
  name: $SERVICE_NAME
spec:
  selector:
    app: $SERVICE_NAME
  ports:
    - protocol: TCP
      port: $SERVICE_PORT
      targetPort: $SERVICE_PORT
EOF

cat > "docs/services/$SERVICE_NAME/README.md" <<EOF
# $SERVICE_NAME

## Responsabilidad

Pendiente.

## Casos de uso

- Pendiente.

## APIs

- Pendiente.

## Eventos publicados

- Pendiente.

## Eventos consumidos

- Pendiente.

## Dependencias

- PostgreSQL
- Kafka
- Redis

## Riesgos técnicos

- Pendiente.
EOF

echo "Servicio $SERVICE_NAME inicializado con estructura hexagonal, documentación y Kubernetes."
