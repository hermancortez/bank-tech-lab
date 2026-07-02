# ADR-001 - Uso de Arquitectura Hexagonal

## Estado

Aceptado

## Contexto

Bank Tech Lab busca simular una plataforma bancaria moderna basada en microservicios.

Los servicios deben ser mantenibles, testeables y desacoplados de frameworks, bases de datos, mensajería y APIs externas.

## Decisión

Se utilizará Arquitectura Hexagonal en los microservicios principales.

La estructura base será:


domain/
application/
infrastructure/
api/
config/
shared/


## Consecuencias
Positivas
- Mejora la mantenibilidad.
- Facilita pruebas unitarias.
- Reduce acoplamiento con frameworks.
- Permite cambiar adaptadores externos con menor impacto.

## Negativas
- Requiere más estructura inicial.
- Puede parecer más complejo en servicios pequeños.
- Requiere disciplina del equipo.

## Alternativas evaluadas
```text
| Alternativa                    | Ventajas                       | Desventajas                  |
| ------------------------------ | ------------------------------ | ---------------------------- |
| Arquitectura por capas clásica | Simple y conocida              | Mayor acoplamiento           |
| Arquitectura Hexagonal         | Desacoplamiento y testabilidad | Mayor estructura             |
| Clean Architecture estricta    | Muy ordenada                   | Puede ser excesiva al inicio |
