package cl.banktech.account;

import cl.banktech.account.infrastructure.persistence.SpringDataAccountRepository;
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
class AccountControllerIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataAccountRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsAccount() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "accountNumber": "ACC-0001",
                                  "type": "CHECKING",
                                  "initialBalance": 100.50
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.accountNumber").value("ACC-0001"))
                .andExpect(jsonPath("$.type").value("CHECKING"))
                .andExpect(jsonPath("$.balance").value(100.50))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    void rejectsDuplicateAccountNumber() throws Exception {
        UUID customerId = UUID.randomUUID();
        createAccount(customerId, "ACC-0002");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "accountNumber": "ACC-0002",
                                  "type": "SAVINGS",
                                  "initialBalance": 25.00
                                }
                                """.formatted(customerId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Account number already exists: ACC-0002"));
    }

    @Test
    void listsAccounts() throws Exception {
        UUID customerId = UUID.randomUUID();
        createAccount(customerId, "ACC-0003");
        createAccount(customerId, "ACC-0004");

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void listsAccountsByCustomerId() throws Exception {
        UUID customerId = UUID.randomUUID();
        createAccount(customerId, "ACC-0005");
        createAccount(UUID.randomUUID(), "ACC-0006");

        mockMvc.perform(get("/accounts/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId").value(customerId.toString()));
    }

    @Test
    void findsAccountById() throws Exception {
        UUID customerId = UUID.randomUUID();
        String id = createAccount(customerId, "ACC-0007");

        mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.accountNumber").value("ACC-0007"));
    }

    @Test
    void returnsNotFoundWhenAccountDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Account not found: " + id));
    }

    @Test
    void creditsAccountBalance() throws Exception {
        UUID customerId = UUID.randomUUID();
        String id = createAccount(customerId, "ACC-0008");

        mockMvc.perform(post("/accounts/{id}/deposit", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 25.50
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.balance").value(35.50));
    }

    @Test
    void debitsAccountBalanceWhenFundsAvailable() throws Exception {
        UUID customerId = UUID.randomUUID();
        String id = createAccount(customerId, "ACC-0009");

        mockMvc.perform(post("/accounts/{id}/withdraw", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 10.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.balance").value(0.00));
    }

    @Test
    void rejectsDebitWhenFundsInsufficient() throws Exception {
        UUID customerId = UUID.randomUUID();
        String id = createAccount(customerId, "ACC-0010");

        mockMvc.perform(post("/accounts/{id}/withdraw", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 100.01
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Insufficient funds for withdrawal"));
    }

    @Test
    void blocksAndClosesAccount() throws Exception {
        UUID customerId = UUID.randomUUID();
        String id = createAccount(customerId, "ACC-0011");

        mockMvc.perform(post("/accounts/{id}/block", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));

        mockMvc.perform(post("/accounts/{id}/close", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": null,
                                  "accountNumber": "",
                                  "type": null,
                                  "initialBalance": -1.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid request fields"));
    }

    private String createAccount(UUID customerId, String accountNumber) throws Exception {
        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "accountNumber": "%s",
                                  "type": "CHECKING",
                                  "initialBalance": 10.00
                                }
                                """.formatted(customerId, accountNumber)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\\\"id\\\":\\\"([^\\\"]+)\\\".*", "$1");
    }
}
