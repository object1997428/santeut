package com.santeut.gateway.filter;

import com.santeut.gateway.authorize.AuthorizationToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private final AuthorizationToken authorizationToken;

    public GlobalFilter(AuthorizationToken authenticateToken) {
        super(Config.class);
        this.authorizationToken = authenticateToken;
    }

    @Override
    public GatewayFilter apply(Config config) {

        // Global PreFilter
        return ((exchange, chain) -> {
            log.debug("Global PreFilter");

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.debug("Global Filter - Request: {}", request.getPath());
            log.debug("Global Filter - Headers: {}", request.getHeaders());
            String path = String.valueOf(request.getPath());

            String accessToken;

            // Header에 Token있는지 확인
            if (request.getHeaders().containsKey("Authorization")) {
                accessToken = request.getHeaders().getFirst("Authorization").substring(7);
                log.debug("accessToken: " + accessToken);

                String userLoginId = authorizationToken.extractAllClaims(accessToken).getSubject();
                log.debug("userLoginId: "+ userLoginId);

                ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                        .header("userLoginId", userLoginId)
                        .build();
                exchange = exchange.mutate().request(serverHttpRequest).build();

                String loginId = exchange.getRequest().getHeaders().getFirst("userLoginId");
                log.debug("loginId: "+ loginId);

                // 인가 (Token 검증)
                boolean validateToken = authorizationToken.validateToken(accessToken);

                if (!validateToken){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    DataBuffer buffer = response.bufferFactory().wrap("유효하지 않은 토큰입니다.".getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer));
                }
                else {

                    // Global PostFilter
                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        log.debug("Global PostFilter");
                        log.debug("Global Filter - Response headers: {}", response.getHeaders());

                    }));
                }
            } else {
                // 인증 (로그인/회원가입)
                if(path.equals("/api/auth/signup") || path.equals("/api/auth/signin")) {

                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    }));
                }
                else {
                    // 임시로 끔
//                    response.setStatusCode(HttpStatus.BAD_REQUEST);
//                    DataBuffer buffer = response.bufferFactory().wrap("토큰이 존재하지 않습니다.".getBytes(StandardCharsets.UTF_8));
//                    return response.writeWith(Mono.just(buffer));

                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    }));
                }
            }
        });
    }

    @Data
    public static class Config {
    }
}
