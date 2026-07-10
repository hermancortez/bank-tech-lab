# Handoff de sesion - 2026-07-10

## Contexto

- Repositorio: `bank-tech-lab`
- Rama: `main`
- Objetivo de la sesion:
  - avanzar lo maximo posible sobre pendientes de `customer-service`
  - guardar contexto
  - dejar commit local y remoto

## Cambios realizados

### Migraciones

- Se agrego Flyway a `customer-service`.
- Se creo la migracion inicial:
  - `backend-java/customer-service/src/main/resources/db/migration/V1__create_customers_table.sql`
- Se cambio JPA de `ddl-auto: update` a `ddl-auto: validate`.
- Se habilito `baseline-on-migrate` para convivir con la base local que ya podia tener tablas creadas antes de Flyway.

### Pruebas HTTP

Se agrego `CustomerControllerIntegrationTests` con cobertura para:

- crear cliente
- rechazar RUT duplicado
- rechazar email duplicado
- listar clientes
- obtener cliente por id
- responder `404` cuando el cliente no existe
- rechazar request invalido

Las pruebas de integracion usan PostgreSQL real mediante Testcontainers. Para Docker Desktop/Docker 29 en WSL se fijo `api.version=1.44` en `src/test/resources/docker-java.properties`.

### Kubernetes

- Se separo configuracion de `customer-service` en:
  - `configmap.yaml`
  - `secret.yaml` local no versionado
  - `secret.yaml.example` versionado como plantilla
- El `deployment.yaml` ahora consume configuracion con `envFrom`.
- Se mantiene `imagePullPolicy: Never` para imagen local cargada con `kind`.
- Se agrego `ingress.yaml` para exponer `/customers` y `/actuator` mediante `ingress-nginx` con host `bank.local`.

### Documentacion

- Se actualizo:
  - `docs/infrastructure/KUBERNETES-LOCAL.md`

### CI

- Se agrego GitHub Actions en `.github/workflows/customer-service-ci.yml`.
- El workflow ejecuta `./mvnw test` para `customer-service`.
- El workflow ejecuta `./mvnw test` para `account-service`.
- El workflow valida manifests Kubernetes con `kubectl apply --dry-run=client --validate=false --recursive -f infra/kubernetes`.

### Account Service

- Se implemento `account-service` en puerto `8082`.
- Se agregaron dominio, casos de uso, persistencia JPA, REST, Flyway y pruebas con Testcontainers.
- Se agregaron manifests Kubernetes para configmap, deployment, service, ingress y plantilla de secret.
- Se configuro `account-service` con schema PostgreSQL propio `account_service` y tabla Flyway `flyway_schema_history_account` para evitar colision con otros servicios en `bankdb`.
- Se desplego en Kubernetes local con imagen `account-service:local`.
- Se creo el secret local `account-service-secret` directamente en el cluster.
- Se valido rollout exitoso, health `UP` y `/accounts` via Ingress con respuesta `200 []`.

## Validaciones realizadas

### Tests

Ejecutado:

```bash
cd backend-java/customer-service
./mvnw test
```

Resultado:

- `BUILD SUCCESS`
- `Tests run: 8, Failures: 0, Errors: 0, Skipped: 0`

### Kubernetes manifests

Ejecutado:

```bash
kubectl apply --dry-run=client -f infra/kubernetes/customer-service
```

Resultado:

- `configmap/customer-service-config created (dry run)`
- `deployment.apps/customer-service configured (dry run)`
- `secret/customer-service-secret created (dry run)`
- `service/customer-service unchanged (dry run)`

### Runtime Kubernetes

Ejecutado:

```bash
docker build -t customer-service:local backend-java/customer-service
kind load docker-image customer-service:local --name laboratorio
kubectl apply -f infra/kubernetes/customer-service
kubectl rollout restart deployment/customer-service
kubectl rollout status deployment/customer-service --timeout=120s
```

Resultado:

- imagen Docker reconstruida correctamente.
- imagen `customer-service:local` cargada en el cluster `kind` `laboratorio`.
- `configmap/customer-service-config` creado.
- `secret/customer-service-secret` creado.
- `deployment.apps/customer-service` configurado.
- `deployment "customer-service" successfully rolled out`.

Estado observado:

```bash
kubectl get pods,svc,deploy -o wide
```

Resultado:

- `pod/customer-service` en estado `1/1 Running`.
- `service/customer-service` expuesto internamente en puerto `8081/TCP`.
- `deployment.apps/customer-service` en estado `1/1 READY`.
- Logs de arranque limpios: Flyway valido 1 migracion, creo baseline sobre la base existente y dejo el schema actualizado.

### Ingress

Ejecutado:

```bash
kubectl apply -f infra/kubernetes/customer-service
curl -H 'Host: bank.local' http://localhost/actuator/health
curl -H 'Host: bank.local' http://localhost/customers
```

Resultado:

- `ingress.networking.k8s.io/customer-service` creado.
- `kubectl describe ingress customer-service` muestra `Address: localhost`.
- `/actuator/health` responde `HTTP/1.1 200` con estado `UP`.
- `/customers` responde `HTTP/1.1 200` con una lista JSON.

## Siguientes pendientes

1. Endurecer reglas de estado de cuenta para operaciones sobre cuentas bloqueadas o cerradas.
2. Agregar auditoria/eventos para movimientos de cuenta.
3. Evaluar autenticacion/autorizacion en el gateway.

## Continuacion: account-service transaccional

Se agregaron endpoints transaccionales en `account-service`:

- `POST /accounts/{id}/deposit`
- `POST /accounts/{id}/withdraw`
- `POST /accounts/{id}/block`
- `POST /accounts/{id}/close`

Validaciones ejecutadas:

- `./mvnw test -DskipTests=false` en `backend-java/account-service`: 12 tests OK.
- `./mvnw test -DskipTests=false` en `backend-java/customer-service`: 8 tests OK.
- `kubectl apply --dry-run=client --validate=false --recursive -f infra/kubernetes`: OK.
- `git diff --check`: OK.
- Rebuild de `account-service:local`, carga en Kind `laboratorio` y rollout de `deployment/account-service`: OK.
- Flujo E2E por Ingress `bank.local`:
  - crear cuenta: OK.
  - depositar `25.50`: balance `125.50`.
  - retirar `10.00`: balance `115.50`.
  - bloquear: status `BLOCKED`.
  - cerrar: status `CLOSED`.

## Continuacion: api-gateway

Se agrego `backend-java/api-gateway` como entrada HTTP unica para:

- `/customers/** -> customer-service`
- `/accounts/** -> account-service`

Cambios realizados:

- Servicio Spring Boot `api-gateway` en puerto `8080`.
- Proxy REST con `RestTemplate`, preservando metodo/body/status y filtrando headers hop-by-hop.
- Tests unitarios y de contexto para rutas, errores downstream y regresion de `Transfer-Encoding`.
- Manifests Kubernetes para `api-gateway`.
- Ingress publico movido a `api-gateway`.
- Manifests de Ingress directos de `customer-service` y `account-service` retirados del repo.
- CI actualizado para ejecutar tests del gateway.

Validaciones ejecutadas:

- `./mvnw test` en `backend-java/api-gateway`: 5 tests OK.
- `./mvnw test` en `backend-java/customer-service`: 8 tests OK.
- `./mvnw test` en `backend-java/account-service`: 12 tests OK.
- `kubectl apply --dry-run=client --validate=false --recursive -f infra/kubernetes`: OK.
- `git diff --check`: OK.
- Rebuild de `api-gateway:local`, carga en Kind `laboratorio` y rollout de `deployment/api-gateway`: OK.
- Se eliminaron los Ingress vivos `customer-service` y `account-service`.
- Flujo E2E por gateway `bank.local`:
  - `GET /actuator/health`: OK.
  - `GET /customers`: OK.
  - `GET /accounts`: OK.
  - `POST /accounts`: OK.
  - `POST /accounts/{id}/deposit`: balance actualizado a `60.00`.

## Continuacion: seguridad api-gateway

Se completo el punto 3: autenticacion/autorizacion basica en `api-gateway`.

Cambios realizados:

- Se agrego autenticacion por header `X-API-Key`.
- Se definieron dos llaves logicas:
  - `viewer`: permite lecturas (`GET`, `HEAD`, `OPTIONS`).
  - `operator`: permite lecturas y escrituras (`POST`, `PUT`, `PATCH`, `DELETE`).
- `/actuator/health` queda publico para probes de Kubernetes.
- El gateway no reenvia `X-API-Key` a los servicios internos.
- Se agregaron propiedades `gateway.auth.*` y variables de entorno:
  - `GATEWAY_AUTH_ENABLED`
  - `GATEWAY_AUTH_VIEWER_API_KEY`
  - `GATEWAY_AUTH_OPERATOR_API_KEY`
- Se agrego `api-gateway-secret` para Kubernetes local y `secret.yaml.example` versionado.
- `infra/kubernetes/api-gateway/secret.yaml` queda ignorado por git.
- Se actualizo documentacion de `api-gateway` y Kubernetes local.

Validaciones ejecutadas:

- `./mvnw test` en `backend-java/api-gateway`: 11 tests OK.
- `./mvnw test` en `backend-java/customer-service`: 8 tests OK.
- `./mvnw test` en `backend-java/account-service`: 12 tests OK.
- `kubectl apply --dry-run=client --validate=false --recursive -f infra/kubernetes`: OK.
- `git diff --check`: OK.
- Se creo/actualizo secret local `api-gateway-secret` con llaves `dev-viewer-key` y `dev-operator-key`.
- Rebuild de `api-gateway:local`, carga en Kind `laboratorio` y rollout de `deployment/api-gateway`: OK.
- Estado runtime:
  - `account-service`: `1/1 Running`.
  - `customer-service`: `1/1 Running`.
  - `api-gateway`: `1/1 Running`.
  - Ingress unico publico: `api-gateway`, host `bank.local`, address `localhost`.
- Flujo E2E por gateway `bank.local`:
  - `GET /actuator/health` sin API key: `200 UP`.
  - `GET /customers` sin API key: `401`.
  - `GET /customers` con `dev-viewer-key`: `200`.
  - `GET /accounts` con `dev-viewer-key`: `200`.
  - `POST /accounts` con `dev-viewer-key`: `403`.
  - `POST /accounts` con `dev-operator-key`: `201`.
  - `POST /accounts/{id}/deposit` con `dev-operator-key`: `200`, balance `125.50`.

Pendientes actualizados:

1. Endurecer reglas de estado de cuenta para operaciones sobre cuentas bloqueadas o cerradas.
2. Agregar auditoria/eventos para movimientos de cuenta.
3. Reemplazar API keys estaticas por `auth-service`/JWT cuando se implemente identidad real.
