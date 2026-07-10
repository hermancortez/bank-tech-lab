package cl.banktech.gateway;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "gateway.routes")
public record GatewayRoutesProperties(
        @NotNull URI customerServiceUrl,
        @NotNull URI accountServiceUrl
) {
}
