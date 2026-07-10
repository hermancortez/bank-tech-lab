package cl.banktech.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    public static final String API_KEY_HEADER = "X-API-Key";

    private static final Set<String> SAFE_METHODS = Set.of(
            HttpMethod.GET.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.OPTIONS.name()
    );

    private final GatewayAuthProperties auth;

    public GatewayAuthenticationFilter(GatewayAuthProperties auth) {
        this.auth = auth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!auth.enabled() || isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);
        if (!isKnownApiKey(apiKey)) {
            writeProblem(response, HttpStatus.UNAUTHORIZED, "Missing or invalid API key");
            return;
        }

        if (requiresOperator(request) && !isOperatorApiKey(apiKey)) {
            writeProblem(response, HttpStatus.FORBIDDEN, "API key is not allowed to perform this operation");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator/health");
    }

    private boolean isKnownApiKey(String apiKey) {
        return matchesConfiguredKey(apiKey, auth.viewerApiKey()) || isOperatorApiKey(apiKey);
    }

    private boolean isOperatorApiKey(String apiKey) {
        return matchesConfiguredKey(apiKey, auth.operatorApiKey());
    }

    private boolean matchesConfiguredKey(String apiKey, String configuredApiKey) {
        return StringUtils.hasText(apiKey)
                && StringUtils.hasText(configuredApiKey)
                && apiKey.equals(configuredApiKey);
    }

    private boolean requiresOperator(HttpServletRequest request) {
        return !SAFE_METHODS.contains(request.getMethod());
    }

    private void writeProblem(HttpServletResponse response, HttpStatus status, String detail) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json");
        response.getWriter().write("""
                {"status":%d,"detail":"%s"}\
                """.formatted(status.value(), detail));
    }
}
