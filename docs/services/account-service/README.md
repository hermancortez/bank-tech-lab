# account-service

## Responsabilidad

Gestiona cuentas bancarias asociadas a clientes.

## Casos de uso

- Crear cuenta.
- Listar cuentas.
- Buscar cuenta por id.
- Listar cuentas por cliente.
- Abonar saldo.
- Retirar saldo.
- Bloquear cuenta.
- Cerrar cuenta.

## API local

```text
POST /accounts
GET /accounts
GET /accounts/{id}
GET /accounts/customer/{customerId}
POST /accounts/{id}/deposit
POST /accounts/{id}/withdraw
POST /accounts/{id}/block
POST /accounts/{id}/close
```
