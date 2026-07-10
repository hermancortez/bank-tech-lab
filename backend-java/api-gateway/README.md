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
GATEWAY_AUTH_ENABLED
GATEWAY_AUTH_VIEWER_API_KEY
GATEWAY_AUTH_OPERATOR_API_KEY
```

## Seguridad

El gateway protege `/customers/**` y `/accounts/**` con el header `X-API-Key`.
Las lecturas aceptan la key viewer u operator. Las escrituras requieren la key
operator. `/actuator/health` queda publico para probes.
