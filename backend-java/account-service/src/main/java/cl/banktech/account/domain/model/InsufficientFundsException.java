package cl.banktech.account.domain.model;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Insufficient funds for withdrawal");
    }
}
