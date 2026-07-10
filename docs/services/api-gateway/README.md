# api-gateway

## Responsabilidad

Centraliza la entrada HTTP hacia los servicios del laboratorio bancario.

## Rutas

```text
/customers/** -> customer-service
/accounts/**  -> account-service
```

## Seguridad

```text
GET/HEAD/OPTIONS /customers/** o /accounts/** -> X-API-Key viewer u operator
POST/PUT/PATCH/DELETE /customers/** o /accounts/** -> X-API-Key operator
/actuator/health -> publico
```

## Kubernetes local

```bash
docker build -t api-gateway:local backend-java/api-gateway
kind load docker-image api-gateway:local --name laboratorio
kubectl apply -f infra/kubernetes/api-gateway
kubectl delete ingress customer-service account-service --ignore-not-found
```

El secret local se crea fuera del repositorio:

```bash
kubectl create secret generic api-gateway-secret \
  --from-literal=GATEWAY_AUTH_VIEWER_API_KEY=dev-viewer-key \
  --from-literal=GATEWAY_AUTH_OPERATOR_API_KEY=dev-operator-key \
  --dry-run=client -o yaml | kubectl apply -f -
```

## Validacion

```bash
curl -H 'Host: bank.local' http://localhost/actuator/health
curl -H 'Host: bank.local' -H 'X-API-Key: dev-viewer-key' http://localhost/customers
curl -H 'Host: bank.local' -H 'X-API-Key: dev-viewer-key' http://localhost/accounts
```
