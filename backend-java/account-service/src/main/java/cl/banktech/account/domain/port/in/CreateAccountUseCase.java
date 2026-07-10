package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.model.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateAccountUseCase {
    Account create(UUID customerId, String accountNumber, AccountType type, BigDecimal initialBalance);
}
