package cl.banktech.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProxyControllerTests {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ProxyController(restTemplate, new GatewayRoutesProperties(
                    URI.create("http://customer-service:8081"),
                    URI.create("http://account-service:8082")
            )))
            .build();

    @Test
    void forwardsCustomerRequests() throws Exception {
        byte[] response = "[{\"name\":\"Ada\"}]".getBytes(StandardCharsets.UTF_8);
        when(restTemplate.exchange(
                eq(URI.create("http://customer-service:8081/customers?status=active")),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(byte[].class)
        )).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(get("/customers?status=active"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Ada\"}]"));
    }

    @Test
    void forwardsAccountRequestsWithBody() throws Exception {
        byte[] response = "{\"balance\":125.50}".getBytes(StandardCharsets.UTF_8);
        when(restTemplate.exchange(
                eq(URI.create("http://account-service:8082/accounts/abc/deposit")),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class)
        )).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/accounts/abc/deposit")
                        .contentType("application/json")
                        .content("{\"amount\":25.50}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"balance\":125.50}"));

        verify(restTemplate).exchange(
                eq(URI.create("http://account-service:8082/accounts/abc/deposit")),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class)
        );
    }

    @Test
    void preservesDownstreamErrorStatus() throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        byte[] response = "{\"detail\":\"Account not found\"}".getBytes(StandardCharsets.UTF_8);
        when(restTemplate.exchange(
                eq(URI.create("http://account-service:8082/accounts/missing")),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(byte[].class)
        )).thenThrow(HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                responseHeaders,
                response,
                StandardCharsets.UTF_8
        ));

        mockMvc.perform(get("/accounts/missing"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"detail\":\"Account not found\"}"));
    }

    @Test
    void removesHopByHopResponseHeaders() throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("transfer-encoding", "chunked");
        byte[] response = "[]".getBytes(StandardCharsets.UTF_8);

        when(restTemplate.exchange(
                eq(URI.create("http://customer-service:8081/customers")),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(byte[].class)
        )).thenReturn(new ResponseEntity<>(response, responseHeaders, HttpStatus.OK));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(HttpHeaders.TRANSFER_ENCODING))
                .andExpect(content().json("[]"));
    }
}
