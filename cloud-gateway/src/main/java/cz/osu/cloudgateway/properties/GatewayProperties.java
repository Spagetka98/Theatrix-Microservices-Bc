package cz.osu.cloudgateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("theatrix")
public record GatewayProperties(String jwtPreposition, String validationURI, List<String> excluded_urls) {
}
