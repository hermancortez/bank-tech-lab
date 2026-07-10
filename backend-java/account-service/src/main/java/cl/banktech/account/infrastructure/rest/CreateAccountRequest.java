package cl.banktech.account.infrastructure.rest;

import cl.banktech.account.domain.model.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountRequest(
        @NotNull UUID customerId,
        @NotBlank String accountNumber,
        @NotNull AccountType type,
        @NotNull @DecimalMin("0.00") BigDecimal initialBalance
) {
}
