package cl.banktech.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.auth")
public record GatewayAuthProperties(
        boolean enabled,
        String viewerApiKey,
        String operatorApiKey
) {
}
