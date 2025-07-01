package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class TokenAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthGatewayFilterFactory.class);

    private final WebClient webClient;

    @Autowired
    public TokenAuthGatewayFilterFactory(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/springbootn3op2l20").build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Token");

            if (token == null || token.isEmpty()) {
                return unauthorizedResponse(exchange, 401, "Token不能为空");
            }

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/token/check")
                            .queryParam("token", token)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(response -> {
                        if (response.contains("\"code\":0")) {
                            return chain.filter(exchange);
                        } else {
                            return unauthorizedResponse(exchange, 401, "Token无效或已过期");
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("验证服务异常", e);
                        return unauthorizedResponse(exchange, 401, "验证服务异常: " + e.getMessage());
                    });
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, int code, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"code\":%d,\"msg\":\"%s\"}", code, msg);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
