package cl.banktech.customer.infrastructure.rest;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String rut,
        String name,
        String email,
        LocalDateTime createdAt
) {
}