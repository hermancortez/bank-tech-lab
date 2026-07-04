package cl.banktech.customer.application.usecase;

import cl.banktech.customer.domain.model.Customer;
import cl.banktech.customer.domain.model.CustomerNotFoundException;
import cl.banktech.customer.domain.port.in.GetCustomerUseCase;
import cl.banktech.customer.domain.port.in.ListCustomersUseCase;
import cl.banktech.customer.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QueryCustomerService implements ListCustomersUseCase, GetCustomerUseCase {
    private final CustomerRepositoryPort repository;

    public QueryCustomerService(CustomerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
