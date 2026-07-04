package cl.banktech.customer.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private UUID id;
    private String rut;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public Customer(UUID id, String rut, String name, String email, LocalDateTime createdAt) {
        this.id = id;
        this.rut = rut;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static Customer create(String rut, String name, String email) {
        return new Customer(
                UUID.randomUUID(),
                rut,
                name,
                email,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getRut() {
        return rut;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}