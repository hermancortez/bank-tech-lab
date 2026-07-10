# api-gateway

## Responsabilidad

Centraliza la entrada HTTP hacia los servicios del laboratorio bancario.

## Rutas

```text
/customers/** -> customer-service
/accounts/**  -> account-service
```

## Kubernetes local

```bash
docker build -t api-gateway:local backend-java/api-gateway
kind load docker-image api-gateway:local --name laboratorio
kubectl apply -f infra/kubernetes/api-gateway
kubectl delete ingress customer-service account-service --ignore-not-found
```

## Validacion

```bash
curl -H 'Host: bank.local' http://localhost/actuator/health
curl -H 'Host: bank.local' http://localhost/customers
curl -H 'Host: bank.local' http://localhost/accounts
```
