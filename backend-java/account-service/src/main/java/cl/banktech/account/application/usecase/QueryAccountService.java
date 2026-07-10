package cl.banktech.account.application.usecase;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.model.AccountNotFoundException;
import cl.banktech.account.domain.port.in.GetAccountUseCase;
import cl.banktech.account.domain.port.in.ListAccountsUseCase;
import cl.banktech.account.domain.port.in.ListCustomerAccountsUseCase;
import cl.banktech.account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QueryAccountService implements ListAccountsUseCase, ListCustomerAccountsUseCase, GetAccountUseCase {

    private final AccountRepositoryPort repository;

    public QueryAccountService(AccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Account> findByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Account findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
