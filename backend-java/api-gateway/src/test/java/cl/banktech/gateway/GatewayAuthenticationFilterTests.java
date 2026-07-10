package cl.banktech.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GatewayAuthenticationFilterTests {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ProtectedController())
            .addFilters(new GatewayAuthenticationFilter(new GatewayAuthProperties(
                    true,
                    "viewer-test-key",
                    "operator-test-key"
            )))
            .build();

    @Test
    void allowsHealthWithoutApiKey() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void rejectsMissingApiKey() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(content().string(containsString("Missing or invalid API key")));
    }

    @Test
    void rejectsBlankApiKeyWhenConfigurationIsBlank() throws Exception {
        MockMvc blankConfiguredMockMvc = MockMvcBuilders
                .standaloneSetup(new ProtectedController())
                .addFilters(new GatewayAuthenticationFilter(new GatewayAuthProperties(true, "", "")))
                .build();

        blankConfiguredMockMvc.perform(get("/customers")
                        .header(GatewayAuthenticationFilter.API_KEY_HEADER, ""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsReadWithViewerApiKey() throws Exception {
        mockMvc.perform(get("/customers")
                        .header(GatewayAuthenticationFilter.API_KEY_HEADER, "viewer-test-key"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void rejectsWriteWithViewerApiKey() throws Exception {
        mockMvc.perform(post("/accounts")
                        .header(GatewayAuthenticationFilter.API_KEY_HEADER, "viewer-test-key"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("not allowed")));
    }

    @Test
    void allowsWriteWithOperatorApiKey() throws Exception {
        mockMvc.perform(post("/accounts")
                        .header(GatewayAuthenticationFilter.API_KEY_HEADER, "operator-test-key"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @RestController
    private static class ProtectedController {

        @GetMapping({"/actuator/health", "/customers"})
        String read() {
            return "ok";
        }

        @PostMapping("/accounts")
        String write() {
            return "ok";
        }
    }
}
