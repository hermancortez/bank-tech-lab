package cl.banktech.customer_service;

import cl.banktech.customer.infrastructure.persistence.SpringDataCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataCustomerRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rut": "11111111-1",
                                  "name": "Ana Perez",
                                  "email": "ana.perez@banktech.cl"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.name").value("Ana Perez"))
                .andExpect(jsonPath("$.email").value("ana.perez@banktech.cl"))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    void rejectsDuplicateRut() throws Exception {
        createCustomer("22222222-2", "Cliente Uno", "cliente.uno@banktech.cl");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rut": "22222222-2",
                                  "name": "Cliente Dos",
                                  "email": "cliente.dos@banktech.cl"
                                }
                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Customer RUT already exists"));
    }

    @Test
    void rejectsDuplicateEmail() throws Exception {
        createCustomer("33333333-3", "Cliente Uno", "duplicado@banktech.cl");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rut": "44444444-4",
                                  "name": "Cliente Dos",
                                  "email": "duplicado@banktech.cl"
                                }
                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Customer email already exists"));
    }

    @Test
    void listsCustomers() throws Exception {
        createCustomer("55555555-5", "Cliente Uno", "uno@banktech.cl");
        createCustomer("66666666-6", "Cliente Dos", "dos@banktech.cl");

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findsCustomerById() throws Exception {
        String id = createCustomer("77777777-7", "Cliente Buscado", "buscado@banktech.cl");

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.rut").value("77777777-7"))
                .andExpect(jsonPath("$.name").value("Cliente Buscado"))
                .andExpect(jsonPath("$.email").value("buscado@banktech.cl"));
    }

    @Test
    void returnsNotFoundWhenCustomerDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Customer not found: " + id));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rut": "",
                                  "name": "",
                                  "email": "invalid-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid request fields"));
    }

    private String createCustomer(String rut, String name, String email) throws Exception {
        String response = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rut": "%s",
                                  "name": "%s",
                                  "email": "%s"
                                }
                                """.formatted(rut, name, email)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\\\"id\\\":\\\"([^\\\"]+)\\\".*", "$1");
    }
}
