package cl.banktech.account.infrastructure.rest;

import cl.banktech.account.domain.model.Account;
import cl.banktech.account.domain.port.in.BlockAccountUseCase;
import cl.banktech.account.domain.port.in.CloseAccountUseCase;
import cl.banktech.account.domain.port.in.CreateAccountUseCase;
import cl.banktech.account.domain.port.in.DepositAccountUseCase;
import cl.banktech.account.domain.port.in.GetAccountUseCase;
import cl.banktech.account.domain.port.in.ListAccountsUseCase;
import cl.banktech.account.domain.port.in.ListCustomerAccountsUseCase;
import cl.banktech.account.domain.port.in.WithdrawAccountUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final ListCustomerAccountsUseCase listCustomerAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final DepositAccountUseCase depositAccountUseCase;
    private final WithdrawAccountUseCase withdrawAccountUseCase;
    private final BlockAccountUseCase blockAccountUseCase;
    private final CloseAccountUseCase closeAccountUseCase;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            ListAccountsUseCase listAccountsUseCase,
            ListCustomerAccountsUseCase listCustomerAccountsUseCase,
            GetAccountUseCase getAccountUseCase,
            DepositAccountUseCase depositAccountUseCase,
            WithdrawAccountUseCase withdrawAccountUseCase,
            BlockAccountUseCase blockAccountUseCase,
            CloseAccountUseCase closeAccountUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
        this.listCustomerAccountsUseCase = listCustomerAccountsUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.depositAccountUseCase = depositAccountUseCase;
        this.withdrawAccountUseCase = withdrawAccountUseCase;
        this.blockAccountUseCase = blockAccountUseCase;
        this.closeAccountUseCase = closeAccountUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        Account account = createAccountUseCase.create(
                request.customerId(),
                request.accountNumber(),
                request.type(),
                request.initialBalance()
        );

        return toResponse(account);
    }

    @GetMapping
    public List<AccountResponse> findAll() {
        return listAccountsUseCase.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable UUID id) {
        return toResponse(getAccountUseCase.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountResponse> findByCustomerId(@PathVariable UUID customerId) {
        return listCustomerAccountsUseCase.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable UUID id, @Valid @RequestBody DepositRequest request) {
        return toResponse(depositAccountUseCase.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable UUID id, @Valid @RequestBody WithdrawRequest request) {
        return toResponse(withdrawAccountUseCase.withdraw(id, request.amount()));
    }

    @PostMapping("/{id}/block")
    public AccountResponse block(@PathVariable UUID id) {
        return toResponse(blockAccountUseCase.block(id));
    }

    @PostMapping("/{id}/close")
    public AccountResponse close(@PathVariable UUID id) {
        return toResponse(closeAccountUseCase.close(id));
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}
