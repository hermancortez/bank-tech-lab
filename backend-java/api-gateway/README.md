# api-gateway

Gateway HTTP para exponer una entrada unica a los microservicios locales.

## Puerto

```text
8080
```

## Rutas

```text
/customers/** -> customer-service
/accounts/**  -> account-service
```

## Variables

```text
SERVER_PORT
GATEWAY_ROUTES_CUSTOMER_SERVICE_URL
GATEWAY_ROUTES_ACCOUNT_SERVICE_URL
```
