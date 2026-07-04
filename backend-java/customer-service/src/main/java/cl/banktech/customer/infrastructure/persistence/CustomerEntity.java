package cl.banktech.customer.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private UUID id;

    private String rut;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    protected CustomerEntity() {
    }

    public CustomerEntity(UUID id, String rut, String name, String email, LocalDateTime createdAt) {
        this.id = id;
        this.rut = rut;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
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