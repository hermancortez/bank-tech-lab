# Coding Standards

## Objetivo

Definir estándares mínimos de desarrollo para los microservicios Java de Bank Tech Lab.

---

## Java

- Usar Java 21.
- Preferir programación orientada a objetos clara y simple.
- Usar `record` para DTOs inmutables.
- Evitar lógica de negocio en controllers.
- Evitar lógica de negocio en entidades JPA.
- Evitar clases utilitarias innecesarias.
- Preferir inyección por constructor.
- Evitar `@Autowired` en atributos.
- Evitar métodos demasiado largos.
- Evitar duplicación de código.

---

## Spring Boot

- Controllers solo deben exponer endpoints.
- Services deben coordinar casos de uso.
- Repositories deben acceder a datos.
- Configuraciones deben estar bajo paquete `config`.
- Usar perfiles por ambiente.
- Exponer Actuator para health y métricas.
- Documentar APIs con OpenAPI.

---

## Arquitectura

- Aplicar arquitectura hexagonal.
- Separar dominio, aplicación e infraestructura.
- El dominio no debe depender de Spring.
- Los adaptadores deben depender de los puertos.
- Las decisiones importantes deben registrarse como ADR.