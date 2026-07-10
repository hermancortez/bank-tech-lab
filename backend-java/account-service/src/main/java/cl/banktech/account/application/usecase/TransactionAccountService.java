package cl.banktech.account.application.usecase;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.model.AccountNotFoundException;
import cl.banktech.account.domain.port.in.BlockAccountUseCase;
import cl.banktech.account.domain.port.in.CloseAccountUseCase;
import cl.banktech.account.domain.port.in.DepositAccountUseCase;
import cl.banktech.account.domain.port.in.WithdrawAccountUseCase;
import cl.banktech.account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class TransactionAccountService implements DepositAccountUseCase, WithdrawAccountUseCase, BlockAccountUseCase, CloseAccountUseCase {

    private final AccountRepositoryPort repository;

    public TransactionAccountService(AccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Account deposit(UUID accountId, BigDecimal amount) {
        Account account = findById(accountId);
        return repository.save(account.deposit(amount));
    }

    @Override
    public Account withdraw(UUID accountId, BigDecimal amount) {
        Account account = findById(accountId);
        return repository.save(account.withdraw(amount));
    }

    @Override
    public Account block(UUID accountId) {
        Account account = findById(accountId);
        return repository.save(account.block());
    }

    @Override
    public Account close(UUID accountId) {
        Account account = findById(accountId);
        return repository.save(account.close());
    }

    private Account findById(UUID accountId) {
        return repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
