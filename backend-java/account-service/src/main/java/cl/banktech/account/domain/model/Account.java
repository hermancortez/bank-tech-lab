package cl.banktech.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final UUID customerId;
    private final String accountNumber;
    private final AccountType type;
    private final BigDecimal balance;
    private final AccountStatus status;
    private final LocalDateTime createdAt;

    public Account(
            UUID id,
            UUID customerId,
            String accountNumber,
            AccountType type,
            BigDecimal balance,
            AccountStatus status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Account create(UUID customerId, String accountNumber, AccountType type, BigDecimal initialBalance) {
        return new Account(
                UUID.randomUUID(),
                customerId,
                accountNumber,
                type,
                initialBalance,
                AccountStatus.ACTIVE,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
