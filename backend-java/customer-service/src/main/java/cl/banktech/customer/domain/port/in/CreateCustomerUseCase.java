package cl.banktech.customer.domain.port.in;

import cl.banktech.customer.domain.model.Customer;

public interface CreateCustomerUseCase {
    Customer create(String rut, String name, String email);
}