package cl.banktech.customer.application.usecase;

import cl.banktech.customer.domain.model.Customer;
import cl.banktech.customer.domain.port.in.CreateCustomerUseCase;
import cl.banktech.customer.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort repository;

    public CreateCustomerService(CustomerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(String rut, String name, String email) {
        if (repository.existsByRut(rut)) {
            throw new IllegalArgumentException("Customer RUT already exists");
        }

        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Customer email already exists");
        }

        Customer customer = Customer.create(rut, name, email);
        return repository.save(customer);
    }
}
