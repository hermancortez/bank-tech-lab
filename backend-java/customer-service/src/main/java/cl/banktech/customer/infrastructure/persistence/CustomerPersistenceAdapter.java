package cl.banktech.customer.infrastructure.persistence;

import cl.banktech.customer.domain.model.Customer;
import cl.banktech.customer.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

    private final SpringDataCustomerRepository repository;

    public CustomerPersistenceAdapter(SpringDataCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByRut(String rut) {
        return repository.existsByRut(rut);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Customer findById(UUID id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    private CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.getId(),
                customer.getRut(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(
                entity.getId(),
                entity.getRut(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreatedAt()
        );
    }
}