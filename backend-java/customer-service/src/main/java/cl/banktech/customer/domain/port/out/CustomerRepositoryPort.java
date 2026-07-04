package cl.banktech.customer.domain.port.out;

import cl.banktech.customer.domain.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    List<Customer> findAll();
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    Customer findById(UUID id);
}