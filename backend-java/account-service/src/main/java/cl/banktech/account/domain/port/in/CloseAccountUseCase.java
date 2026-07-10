package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;

import java.util.UUID;

public interface CloseAccountUseCase {
    Account close(UUID accountId);
}
