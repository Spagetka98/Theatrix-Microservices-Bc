package cz.osu.cloudgateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.osu.cloudgateway.model.enums.EHeaders;
import cz.osu.cloudgateway.model.response.AuthExceptionResponse;
import cz.osu.cloudgateway.model.response.GatewayValidationResponse;
import cz.osu.cloudgateway.properties.GatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {
    private final GatewayProperties gatewayProperties;
    private final WebClient.Builder webClientBuilder;

    public AuthenticationPrefilter(GatewayProperties gatewayProperties, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.gatewayProperties = gatewayProperties;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String bearerToken = request.getHeaders().getFirst(EHeaders.AUTHORIZATION.getValue());

            if (gatewayProperties.excluded_urls().stream().noneMatch(uri -> request.getURI().getPath().contains(uri))) {
                return webClientBuilder.build().get()
                        .uri(gatewayProperties.validationURI())
                        .header(EHeaders.AUTHORIZATION.getValue(), bearerToken)
                        .retrieve().bodyToMono(GatewayValidationResponse.class)
                        .map(response -> {
                            exchange.getRequest().mutate().header(EHeaders.USERNAME.getValue(), response.username());
                            exchange.getRequest().mutate().header(EHeaders.USER_ID.getValue(), response.userID());
                            exchange.getRequest().mutate().header(EHeaders.ROLES.getValue(), response.roles().toString());

                            return exchange;
                        }).flatMap(chain::filter).onErrorResume(error -> {
                            HttpStatus errorCode;
                            String errorMsg;

                            if (error instanceof WebClientResponseException webClientException) {
                                errorCode = webClientException.getStatusCode();
                                errorMsg = webClientException.getStatusText();
                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                                errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
                            }
//
                            return onError(exchange, errorCode, errorMsg);
                        });
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMsg) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        try {
            ObjectMapper objMapper = new ObjectMapper();

            response.getHeaders().add(EHeaders.CONTENT_TYPE.getValue(), "application/json");

            AuthExceptionResponse data = new AuthExceptionResponse(httpStatus.value(), errorMsg, Instant.now().toString());

            byte[] byteData = objMapper.writeValueAsBytes(data);
            return response.writeWith(Mono.just(byteData).map(dataBufferFactory::wrap));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.setComplete();
    }

    public static class Config {
    }
}
