# Handoff de sesion - 2026-07-09

## Contexto

- Repositorio: `bank-tech-lab`
- Rama de trabajo: `main`
- Objetivo retomado:
  - continuar con Kubernetes local
  - dejar `k9s` listo para observar el cluster
  - desplegar `customer-service` en `kind`
  - guardar contexto y publicar cambios local/remoto

## Estado inicial encontrado

- `k9s` ya estaba instalado:
  - version `v0.51.0`
  - binario en `/home/herman/.local/bin/k9s`
- `kubectl` apuntaba a:
  - contexto `kind-laboratorio`
- El cluster `kind` estaba sano:
  - nodo `laboratorio-control-plane` en `Ready`
  - namespaces base activos
  - `ingress-nginx` en `Running`
- Docker local tenia la infraestructura base arriba:
  - `bank-postgres`
  - `bank-redis`
  - `bank-kafka`
  - `bank-kafka-ui`

## Hallazgos

- Kubernetes no tenia microservicios de negocio desplegados.
- `customer-service` existia y funcionaba como aplicacion Java, pero no tenia:
  - `Dockerfile`
  - manifests Kubernetes propios
- `docker-compose.yml` define PostgreSQL con:
  - usuario `bankuser`
  - password `bankpass`
  - base `bankdb`
- `customer-service` todavia apuntaba a credenciales antiguas:
  - usuario `admin`
  - password `123456`

## Cambios realizados

### k9s

- Se configuro vista inicial `pods` en:
  - `~/.config/k9s/config.yaml`

### Documentacion

- Se agrego guia:
  - `docs/infrastructure/KUBERNETES-LOCAL.md`
- Se actualizo `README.md` con seccion de Kubernetes local.

### customer-service

- Se agrego:
  - `backend-java/customer-service/Dockerfile`
- Se corrigieron credenciales por defecto en:
  - `backend-java/customer-service/src/main/resources/application.yml`
- Se agregaron manifests:
  - `infra/kubernetes/customer-service/deployment.yaml`
  - `infra/kubernetes/customer-service/service.yaml`

El deployment usa:

- imagen `customer-service:local`
- puerto `8081`
- `imagePullPolicy: Never`
- datasource hacia `host.docker.internal:5432`
- probes sobre `/actuator/health`

## Validaciones realizadas

### Maven

Ejecutado en `backend-java/customer-service`:

```bash
./mvnw test
```

Resultado:

- `BUILD SUCCESS`
- `Tests run: 1, Failures: 0, Errors: 0, Skipped: 0`

### Docker

Se construyo:

```bash
docker build -t customer-service:local backend-java/customer-service
```

Resultado:

- imagen `customer-service:local` construida correctamente

### kind

Se cargo la imagen:

```bash
kind load docker-image customer-service:local --name laboratorio
```

Resultado:

- imagen disponible dentro del nodo `laboratorio-control-plane`

### Kubernetes

Se aplico:

```bash
kubectl apply -f infra/kubernetes/customer-service
```

Luego se reinicio rollout con la imagen reconstruida:

```bash
kubectl rollout restart deployment/customer-service
kubectl rollout status deployment/customer-service --timeout=120s
```

Resultado:

- `deployment "customer-service" successfully rolled out`
- pod final:
  - `1/1 Running`
  - `0` restarts

### Health y endpoints

Con port-forward temporal:

```bash
kubectl port-forward svc/customer-service 8081:8081
```

Validaciones:

```bash
curl -sS http://localhost:8081/actuator/health
curl -sS http://localhost:8081/customers
```

Resultados:

```json
{"status":"UP","groups":["liveness","readiness"]}
```

```json
[]
```

## Estado final esperado

Comando:

```bash
kubectl get pods,svc,deploy -o wide
```

Debe mostrar:

- `pod/customer-service-*` en `1/1 Running`
- `service/customer-service` en puerto `8081/TCP`
- `deployment/customer-service` en `1/1`

## Como observar con k9s

```bash
k9s --context kind-laboratorio
```

Vistas utiles:

- `:pods`
- `:deploy`
- `:svc`
- `:events`

En un pod:

- `l` para logs
- `d` para describe

## Siguiente paso recomendado

1. Externalizar credenciales con `Secret` y `ConfigMap`.
2. Agregar `Ingress` para `customer-service`.
3. Crear manifests faltantes para `account-service` cuando el servicio este implementado.
4. Agregar migraciones con Flyway o Liquibase para dejar de depender de `ddl-auto: update`.
5. Agregar pruebas HTTP/integracion para `POST /customers`, `GET /customers` y `GET /customers/{id}`.
