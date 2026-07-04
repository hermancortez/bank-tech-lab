#!/usr/bin/env bash
set -e

BASE_DIR="academy"

mkdir -p "$BASE_DIR"

create_file() {
  local file="$1"
  local title="$2"

  mkdir -p "$(dirname "$file")"

  # La academia puede contener apuntes personales: nunca los sobrescribimos.
  [ -f "$file" ] && return

  cat > "$file" <<EOF
# $title

## ¿Qué es?

Pendiente.

## ¿Por qué importa en banca?

Pendiente.

## ¿Cuándo usarlo?

Pendiente.

## ¿Cuándo NO usarlo?

Pendiente.

## Ejemplo práctico

Pendiente.

## Preguntas típicas de entrevista

1. Pendiente.
2. Pendiente.
3. Pendiente.

## Resumen rápido

Pendiente.
EOF
}

create_file "$BASE_DIR/00-roadmap/README.md" "Roadmap"

create_file "$BASE_DIR/01-java/01-jvm.md" "JVM"
create_file "$BASE_DIR/01-java/02-memoria.md" "Memoria en Java"
create_file "$BASE_DIR/01-java/03-threads.md" "Threads"
create_file "$BASE_DIR/01-java/04-collections.md" "Collections"
create_file "$BASE_DIR/01-java/05-streams.md" "Streams"
create_file "$BASE_DIR/01-java/06-lambdas.md" "Lambdas"
create_file "$BASE_DIR/01-java/07-generics.md" "Generics"
create_file "$BASE_DIR/01-java/08-performance.md" "Performance"
create_file "$BASE_DIR/01-java/09-preguntas.md" "Preguntas Java"

create_file "$BASE_DIR/02-spring/01-spring-core.md" "Spring Core"
create_file "$BASE_DIR/02-spring/02-spring-boot.md" "Spring Boot"
create_file "$BASE_DIR/02-spring/03-spring-security.md" "Spring Security"
create_file "$BASE_DIR/02-spring/04-jpa.md" "JPA e Hibernate"
create_file "$BASE_DIR/02-spring/05-transacciones.md" "Transacciones"
create_file "$BASE_DIR/02-spring/06-openapi.md" "OpenAPI"
create_file "$BASE_DIR/02-spring/07-actuator.md" "Spring Actuator"
create_file "$BASE_DIR/02-spring/08-preguntas.md" "Preguntas Spring"

create_file "$BASE_DIR/03-arquitectura/01-clean.md" "Clean Architecture"
create_file "$BASE_DIR/03-arquitectura/02-hexagonal.md" "Arquitectura Hexagonal"
create_file "$BASE_DIR/03-arquitectura/03-ddd.md" "Domain Driven Design"
create_file "$BASE_DIR/03-arquitectura/04-cqrs.md" "CQRS"
create_file "$BASE_DIR/03-arquitectura/05-saga.md" "Saga Pattern"
create_file "$BASE_DIR/03-arquitectura/06-outbox.md" "Outbox Pattern"
create_file "$BASE_DIR/03-arquitectura/07-patrones.md" "Patrones de Diseño"
create_file "$BASE_DIR/03-arquitectura/08-entrevista.md" "Entrevista Arquitectura"

create_file "$BASE_DIR/04-microservicios/01-rest.md" "REST APIs"
create_file "$BASE_DIR/04-microservicios/02-versionado.md" "Versionado de APIs"
create_file "$BASE_DIR/04-microservicios/03-api-gateway.md" "API Gateway"
create_file "$BASE_DIR/04-microservicios/04-eureka.md" "Eureka Service Discovery"
create_file "$BASE_DIR/04-microservicios/05-config-server.md" "Config Server"
create_file "$BASE_DIR/04-microservicios/06-circuit-breaker.md" "Circuit Breaker"
create_file "$BASE_DIR/04-microservicios/07-resilience.md" "Resilience4j"
create_file "$BASE_DIR/04-microservicios/08-idempotencia.md" "Idempotencia"
create_file "$BASE_DIR/04-microservicios/09-entrevista.md" "Entrevista Microservicios"

create_file "$BASE_DIR/05-cloud/01-docker.md" "Docker"
create_file "$BASE_DIR/05-cloud/02-cloud.md" "Cloud"
create_file "$BASE_DIR/05-cloud/03-helm.md" "Helm"
create_file "$BASE_DIR/05-cloud/04-ingress.md" "Ingress"
create_file "$BASE_DIR/05-cloud/05-istio.md" "Istio"
create_file "$BASE_DIR/05-cloud/06-service-mesh.md" "Service Mesh"
create_file "$BASE_DIR/05-cloud/07-entrevista.md" "Entrevista Cloud"

create_file "$BASE_DIR/06-kubernetes/01-pods.md" "Pods"
create_file "$BASE_DIR/06-kubernetes/02-deployments.md" "Deployments"
create_file "$BASE_DIR/06-kubernetes/03-services.md" "Services"
create_file "$BASE_DIR/06-kubernetes/04-configmaps.md" "ConfigMaps"
create_file "$BASE_DIR/06-kubernetes/05-secrets.md" "Secrets"
create_file "$BASE_DIR/06-kubernetes/06-hpa.md" "Horizontal Pod Autoscaler"
create_file "$BASE_DIR/06-kubernetes/07-probes.md" "Probes"
create_file "$BASE_DIR/06-kubernetes/08-entrevista.md" "Entrevista Kubernetes"

create_file "$BASE_DIR/07-kafka/01-conceptos.md" "Kafka Conceptos"
create_file "$BASE_DIR/07-kafka/02-topics.md" "Kafka Topics"
create_file "$BASE_DIR/07-kafka/03-particiones.md" "Kafka Particiones"
create_file "$BASE_DIR/07-kafka/04-consumers.md" "Kafka Consumers"
create_file "$BASE_DIR/07-kafka/05-dlq.md" "Dead Letter Queue"
create_file "$BASE_DIR/07-kafka/06-outbox.md" "Outbox con Kafka"
create_file "$BASE_DIR/07-kafka/07-preguntas.md" "Preguntas Kafka"

create_file "$BASE_DIR/08-observabilidad/01-prometheus.md" "Prometheus"
create_file "$BASE_DIR/08-observabilidad/02-grafana.md" "Grafana"
create_file "$BASE_DIR/08-observabilidad/03-opentelemetry.md" "OpenTelemetry"
create_file "$BASE_DIR/08-observabilidad/04-micrometer.md" "Micrometer"
create_file "$BASE_DIR/08-observabilidad/05-jaeger.md" "Jaeger"
create_file "$BASE_DIR/08-observabilidad/06-dynatrace.md" "Dynatrace"
create_file "$BASE_DIR/08-observabilidad/07-preguntas.md" "Preguntas Observabilidad"

create_file "$BASE_DIR/09-devsecops/01-github-actions.md" "GitHub Actions"
create_file "$BASE_DIR/09-devsecops/02-sonarqube.md" "SonarQube"
create_file "$BASE_DIR/09-devsecops/03-testcontainers.md" "Testcontainers"
create_file "$BASE_DIR/09-devsecops/04-jwt.md" "JWT"
create_file "$BASE_DIR/09-devsecops/05-oauth.md" "OAuth2"
create_file "$BASE_DIR/09-devsecops/06-trivy.md" "Trivy"
create_file "$BASE_DIR/09-devsecops/07-owasp.md" "OWASP"

create_file "$BASE_DIR/10-gobierno/01-blueprints.md" "Blueprints"
create_file "$BASE_DIR/10-gobierno/02-golden-path.md" "Golden Path"
create_file "$BASE_DIR/10-gobierno/03-arquetipos.md" "Arquetipos"
create_file "$BASE_DIR/10-gobierno/04-roadmaps.md" "Roadmaps Tecnológicos"
create_file "$BASE_DIR/10-gobierno/05-adr.md" "Architecture Decision Records"
create_file "$BASE_DIR/10-gobierno/06-c4.md" "Modelo C4"
create_file "$BASE_DIR/10-gobierno/07-build-vs-buy.md" "Build vs Buy"
create_file "$BASE_DIR/10-gobierno/08-comites.md" "Comités de Arquitectura"

create_file "$BASE_DIR/11-legacy/01-weblogic.md" "Oracle WebLogic"
create_file "$BASE_DIR/11-legacy/02-jdbc.md" "JDBC"
create_file "$BASE_DIR/11-legacy/03-soap.md" "SOAP"
create_file "$BASE_DIR/11-legacy/04-oracle.md" "Oracle"
create_file "$BASE_DIR/11-legacy/05-migraciones.md" "Migraciones Legacy"

create_file "$BASE_DIR/12-entrevistas/100-java.md" "100 Preguntas Java"
create_file "$BASE_DIR/12-entrevistas/100-spring.md" "100 Preguntas Spring"
create_file "$BASE_DIR/12-entrevistas/100-arquitectura.md" "100 Preguntas Arquitectura"
create_file "$BASE_DIR/12-entrevistas/100-kubernetes.md" "100 Preguntas Kubernetes"
create_file "$BASE_DIR/12-entrevistas/100-kafka.md" "100 Preguntas Kafka"
create_file "$BASE_DIR/12-entrevistas/100-lider-tecnico.md" "100 Preguntas Líder Técnico"
create_file "$BASE_DIR/12-entrevistas/100-arquitecto.md" "100 Preguntas Arquitecto"

create_file "$BASE_DIR/13-proyecto-final/README.md" "Proyecto Final Plataforma Bancaria"

# Estructura objetivo solicitada. Nunca sobrescribe apuntes existentes.
for name in README.md 01-como-ejecutar-spring.md 02-maven-vs-mvnw.md 03-estructura-proyecto-bancario.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/00-onboarding/$name" "$title"
done

for name in 01-java-7-vs-java-21.md 02-encapsulamiento.md 03-acoplamiento-cohesion.md 04-heap-stack.md 05-garbage-collector.md 06-threads.md 07-thread-pool.md 08-asincronia.md 09-collections.md 10-memory-leaks.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/01-java/$name" "$title"
done

for name in 01-spring-framework.md 02-spring-mvc.md 03-spring-boot.md 04-dependency-injection.md 05-spring-data-jpa.md 06-spring-security.md 07-transacciones.md 08-actuator.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/02-spring/$name" "$title"
done

for name in 01-oracle.md 02-jpa-hibernate.md 03-liquibase.md 04-connection-pool.md 05-indices-performance.md 06-procedures-functions.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/03-persistencia/$name" "$title"
done

for name in 01-angularjs.md 02-angular-7-11.md 03-session-storage.md 04-sesiones-base-datos.md 05-interceptors-guards.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/04-frontend-legado/$name" "$title"
done

for name in 01-jenkins.md 02-jenkinsfile.md 03-docker.md 04-kubernetes.md 05-kubectl-pods.md 06-deployments-services.md 07-ingress.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/05-devops/$name" "$title"
done

for name in 01-oci-conceptos.md 02-oci-vs-aws-azure-gcp.md 03-oke.md 04-vcn-subnets.md 05-load-balancer.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/06-cloud-oci/$name" "$title"
done

for name in 01-service-mesh.md 02-istio.md 03-gateway-virtualservice.md 04-destinationrule.md 05-mtls.md 06-canary.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/07-service-mesh/$name" "$title"
done

for name in 01-rest-api.md 02-openapi.md 03-openapi-vs-rest.md 04-versionado-api.md 05-api-gateway.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/08-apis/$name" "$title"
done

for name in 01-hexagonal.md 02-clean-architecture.md 03-ddd.md 04-cqrs.md 05-saga.md 06-outbox.md 07-microservicios.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/09-arquitectura/$name" "$title"
done

for name in 01-blueprints.md 02-golden-paths.md 03-arquetipos.md 04-roadmap-tecnologico.md 05-gobierno-tecnologico.md 06-referencia-arquitectonica.md 07-build-vs-buy.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/10-gobierno-tecnologico/$name" "$title"
done

for name in 01-preguntas-java.md 02-preguntas-spring.md 03-preguntas-kubernetes.md 04-preguntas-jenkins.md 05-preguntas-oracle.md 06-preguntas-arquitectura.md 07-preguntas-lider-tecnico.md; do
  title="$(printf '%s' "${name%.md}" | sed -E 's/^[0-9]+-//; s/-/ /g')"
  create_file "$BASE_DIR/11-entrevistas/$name" "$title"
done


echo "Academia creada correctamente en $BASE_DIR"
