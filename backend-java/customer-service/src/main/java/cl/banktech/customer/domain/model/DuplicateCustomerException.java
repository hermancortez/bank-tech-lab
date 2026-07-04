package cl.banktech.customer.domain.model;

public class DuplicateCustomerException extends RuntimeException {
    public DuplicateCustomerException(String field) {
        super("Customer " + field + " already exists");
    }
}
