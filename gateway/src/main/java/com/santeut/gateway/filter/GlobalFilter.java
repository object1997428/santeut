package com.santeut.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

//        return new GatewayFilter() {
//            @Override
//            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//                ServerHttpRequest request = exchange.getRequest();
//                ServerHttpResponse response = exchange.getResponse();
//
//                log.debug("exchange: " + exchange);
//                log.debug("chain: " + chain);
//
//                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                    log.info("Global Filter End : response code = {}", response.getStatusCode());
//                }));
//            }
//        };

        // Custom PreFilter
        log.debug("Global Filter 진입");

        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.debug("exchange: {}", exchange);
            log.debug("chain: {}", chain);

            log.debug("exchange: "+exchange);
            log.debug("chain: "+exchange);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info(String.valueOf(request.getHeaders()));
            }));
        });

    }

    @Data
    public static class Config{

    }
}
