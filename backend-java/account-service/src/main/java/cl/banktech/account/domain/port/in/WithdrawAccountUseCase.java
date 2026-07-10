package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public interface WithdrawAccountUseCase {
    Account withdraw(UUID accountId, BigDecimal amount);
}
