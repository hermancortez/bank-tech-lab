package cl.banktech.customer.infrastructure.rest;

import cl.banktech.customer.domain.model.Customer;
import cl.banktech.customer.domain.port.in.CreateCustomerUseCase;
import cl.banktech.customer.domain.port.in.GetCustomerUseCase;
import cl.banktech.customer.domain.port.in.ListCustomersUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final ListCustomersUseCase listCustomersUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            ListCustomersUseCase listCustomersUseCase,
            GetCustomerUseCase getCustomerUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.listCustomersUseCase = listCustomersUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
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
        return listCustomersUseCase.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable UUID id) {
        return toResponse(getCustomerUseCase.findById(id));
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
