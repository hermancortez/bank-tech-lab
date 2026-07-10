package cl.banktech.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
public class ProxyController {

    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            "Keep-Alive",
            "Proxy-Authenticate",
            "Proxy-Authorization",
            "TE",
            "Trailer",
            "Upgrade",
            HttpHeaders.CONNECTION,
            HttpHeaders.CONTENT_LENGTH,
            HttpHeaders.HOST,
            HttpHeaders.TRANSFER_ENCODING
    );

    private final RestTemplate restTemplate;
    private final GatewayRoutesProperties routes;

    public ProxyController(RestTemplate restTemplate, GatewayRoutesProperties routes) {
        this.restTemplate = restTemplate;
        this.routes = routes;
    }

    @RequestMapping("/customers/**")
    public ResponseEntity<byte[]> customers(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) byte[] body
    ) {
        return forward(routes.customerServiceUrl(), request, headers, body);
    }

    @RequestMapping("/accounts/**")
    public ResponseEntity<byte[]> accounts(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) byte[] body
    ) {
        return forward(routes.accountServiceUrl(), request, headers, body);
    }

    private ResponseEntity<byte[]> forward(URI targetBase, HttpServletRequest request, HttpHeaders headers, byte[] body) {
        URI target = buildTargetUri(targetBase, request);
        HttpEntity<byte[]> entity = new HttpEntity<>(body, filterHeaders(headers));

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    target,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    byte[].class
            );

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(filterHeaders(response.getHeaders()))
                    .body(response.getBody());
        } catch (RestClientResponseException exception) {
            return ResponseEntity
                    .status(exception.getStatusCode())
                    .headers(filterHeaders(exception.getResponseHeaders()))
                    .body(exception.getResponseBodyAsByteArray());
        }
    }

    private URI buildTargetUri(URI targetBase, HttpServletRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(targetBase)
                .path(request.getRequestURI());

        if (request.getQueryString() != null) {
            builder.query(request.getQueryString());
        }

        return builder.build(true).toUri();
    }

    private HttpHeaders filterHeaders(HttpHeaders source) {
        HttpHeaders filtered = new HttpHeaders();
        if (source == null) {
            return filtered;
        }

        source.forEach((name, values) -> {
            if (!isHopByHopHeader(name)) {
                filtered.put(name, values);
            }
        });

        return filtered;
    }

    private boolean isHopByHopHeader(String headerName) {
        return HOP_BY_HOP_HEADERS.stream()
                .anyMatch(header -> header.equalsIgnoreCase(headerName));
    }
}
