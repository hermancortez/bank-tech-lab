package cl.banktech.account.domain.model;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String accountNumber) {
        super("Account number already exists: " + accountNumber);
    }
}
