package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;

import java.util.List;

public interface ListAccountsUseCase {
    List<Account> findAll();
}
