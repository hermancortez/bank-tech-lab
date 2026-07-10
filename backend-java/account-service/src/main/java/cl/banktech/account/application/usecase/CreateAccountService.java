package cl.banktech.account.application.usecase;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.model.AccountType;
import cl.banktech.account.domain.model.DuplicateAccountException;
import cl.banktech.account.domain.port.in.CreateAccountUseCase;
import cl.banktech.account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepositoryPort repository;

    public CreateAccountService(AccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Account create(UUID customerId, String accountNumber, AccountType type, BigDecimal initialBalance) {
        if (repository.existsByAccountNumber(accountNumber)) {
            throw new DuplicateAccountException(accountNumber);
        }

        Account account = Account.create(customerId, accountNumber, type, initialBalance);
        return repository.save(account);
    }
}
