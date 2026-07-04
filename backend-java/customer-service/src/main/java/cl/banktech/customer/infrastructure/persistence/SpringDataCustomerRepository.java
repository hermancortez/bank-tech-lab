package cl.banktech.customer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
}