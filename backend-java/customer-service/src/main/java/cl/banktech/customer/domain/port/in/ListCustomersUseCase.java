package cl.banktech.customer.domain.port.in;

import cl.banktech.customer.domain.model.Customer;
import java.util.List;

public interface ListCustomersUseCase {
    List<Customer> findAll();
}
