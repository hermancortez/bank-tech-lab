package cl.banktech.account.infrastructure.rest;

import cl.banktech.account.domain.model.AccountStatus;
import cl.banktech.account.domain.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        UUID customerId,
        String accountNumber,
        AccountType type,
        BigDecimal balance,
        AccountStatus status,
        LocalDateTime createdAt
) {
}
