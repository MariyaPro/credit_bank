package com.prokofeva.gateway_api.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Принят {}-запрос {}.", logId, exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() ->
                        log.info("{} -- Получен ответ со статусом {}.", logId, exchange.getResponse().getStatusCode())));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}