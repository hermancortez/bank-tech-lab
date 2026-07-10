# Kubernetes local

## Contexto

El laboratorio usa un cluster local con `kind`.

```bash
kubectl config current-context
```

El contexto esperado es:

```text
kind-laboratorio
```

## Verificacion rapida

```bash
kubectl get nodes -o wide
kubectl get pods,svc,deploy -A -o wide
kubectl get events -A --sort-by=.metadata.creationTimestamp
```

El nodo debe estar en estado `Ready`. En una instalacion base se esperan pods de sistema en `Running`, por ejemplo `coredns`, `kindnet`, `local-path-provisioner` e `ingress-nginx-controller`.

## Uso con k9s

`k9s` permite inspeccionar el cluster desde terminal sin recordar todos los comandos de `kubectl`.

```bash
k9s --context kind-laboratorio
```

Acciones utiles:

- `:pods` para ver pods.
- `:deploy` para ver deployments.
- `:svc` para ver services.
- `:events` para ver eventos.
- `l` sobre un pod para ver logs.
- `d` sobre un recurso para describirlo.
- `0` para ver todos los namespaces.

La configuracion local de `k9s` queda en `~/.config/k9s/config.yaml`. Para este entorno se deja `pods` como vista inicial.

## Estado actual del laboratorio

Docker local levanta la base de infraestructura con:

- `bank-postgres`
- `bank-redis`
- `bank-kafka`
- `bank-kafka-ui`

Kubernetes local tiene el cluster y `ingress-nginx` operativos, pero no despliega automaticamente los microservicios de negocio. Para ver servicios de la aplicacion en `k9s`, primero hay que construir/cargar imagenes al cluster `kind` y aplicar los manifests de `infra/kubernetes`.

Flujo esperado para un servicio:

```bash
docker build -t customer-service:local backend-java/customer-service
kind load docker-image customer-service:local --name laboratorio
kubectl apply -f infra/kubernetes/customer-service
```

En el entorno local, `customer-service` usa PostgreSQL publicado en Docker mediante `host.docker.internal:5432`.

La configuracion del deployment esta separada en:

- `configmap.yaml` para puerto y URL JDBC.
- `secret.yaml` para usuario y password de base de datos. Este archivo es local y no se versiona.
- `secret.yaml.example` como plantilla versionada del secret.
- `deployment.yaml` para la carga de trabajo y probes.
- `service.yaml` para exponer el puerto dentro del cluster.
- `ingress.yaml` para exponer rutas HTTP mediante `ingress-nginx`.

Antes de aplicar los manifests por primera vez, crear el secret local:

```bash
cp infra/kubernetes/customer-service/secret.yaml.example infra/kubernetes/customer-service/secret.yaml
```

Luego editar `secret.yaml` con las credenciales locales de PostgreSQL.

Luego se valida con:

```bash
kubectl get pods,svc,deploy
k9s --context kind-laboratorio
```

## Ingress local

El cluster local expone `ingress-nginx` en los puertos `80` y `443` del host. `customer-service` queda disponible con el host `bank.local` para:

- `/customers`
- `/actuator`

Para usar el nombre local en el navegador o con `curl` sin headers, agregar en el host:

```text
127.0.0.1 bank.local
```

Validacion directa sin modificar `/etc/hosts`:

```bash
curl -H 'Host: bank.local' http://localhost/actuator/health
curl -H 'Host: bank.local' http://localhost/customers
```
