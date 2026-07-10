# Ingress

Reglas de entrada HTTP y terminación TLS de la plataforma.

En el entorno local con `kind`, `customer-service` se expone mediante `ingress-nginx` con el host `bank.local`.

Manifest:

```text
infra/kubernetes/customer-service/ingress.yaml
```

Validacion:

```bash
kubectl apply -f infra/kubernetes/customer-service
curl -H 'Host: bank.local' http://localhost/actuator/health
curl -H 'Host: bank.local' http://localhost/customers
```
