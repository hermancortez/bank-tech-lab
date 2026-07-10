package cl.banktech.account.infrastructure.persistence;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountPersistenceAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repository;

    public AccountPersistenceAdapter(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity saved = repository.save(toEntity(account));
        return toDomain(saved);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Account> findByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    private AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.getId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getCustomerId(),
                entity.getAccountNumber(),
                entity.getType(),
                entity.getBalance(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
