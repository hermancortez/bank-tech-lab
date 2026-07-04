package cl.banktech.customer.infrastructure.rest;

import cl.banktech.customer.domain.model.Customer;
import cl.banktech.customer.domain.port.in.CreateCustomerUseCase;
import cl.banktech.customer.domain.port.out.CustomerRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final CustomerRepositoryPort repository;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            CustomerRepositoryPort repository
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = createCustomerUseCase.create(
                request.rut(),
                request.name(),
                request.email()
        );

        return toResponse(customer);
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getRut(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }
}