package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositAccountUseCase {
    Account deposit(UUID accountId, BigDecimal amount);
}
