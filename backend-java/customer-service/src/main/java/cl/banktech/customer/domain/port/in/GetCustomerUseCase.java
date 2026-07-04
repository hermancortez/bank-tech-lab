package cl.banktech.customer.domain.port.in;

import cl.banktech.customer.domain.model.Customer;
import java.util.UUID;

public interface GetCustomerUseCase {
    Customer findById(UUID id);
}
