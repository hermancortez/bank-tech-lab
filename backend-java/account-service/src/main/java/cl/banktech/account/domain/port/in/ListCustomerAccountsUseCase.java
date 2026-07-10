package cl.banktech.account.domain.port.in;

import cl.banktech.account.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface ListCustomerAccountsUseCase {
    List<Account> findByCustomerId(UUID customerId);
}
