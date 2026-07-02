# Arquitectura General

## Objetivo

Bank Tech Lab implementa una arquitectura basada en microservicios utilizando Arquitectura Hexagonal.

## Principios

- Domain Driven Design
- SOLID
- Clean Architecture
- Event Driven
- API First
- Cloud Native
- Twelve Factor App

## Arquitectura

Frontend

↓

API Gateway

↓

Microservicios

↓

Kafka

↓

PostgreSQL

↓

Redis

↓

Observabilidad

↓

Grafana

↓

Prometheus

↓

OpenTelemetry

## Microservicios

- customer-service
- account-service
- auth-service
- transfer-service
- payment-service
- notification-service
- audit-service
- document-service

## Tecnologías

Java 21

Spring Boot

Spring Security

Spring Cloud

Kafka

Redis

PostgreSQL

Docker

Kubernetes

Helm

Istio