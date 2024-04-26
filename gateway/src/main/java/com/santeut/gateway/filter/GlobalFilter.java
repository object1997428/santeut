package com.santeut.gateway.filter;

import com.santeut.gateway.authorize.AuthorizationToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private final AuthorizationToken authorizationToken;

    public GlobalFilter(AuthorizationToken authenticateToken){
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

            String accessToken = request.getHeaders().getFirst("Authorization");

            if(accessToken == null){

            }

            // 인증 (로그인/회원가입)
            if(path.equals("/api/auth/signUp")){

            }
            else if(path.equals("/api/auth/signIn")){

            }

            // 인가 (Token 검증)

            boolean validateToken = authorizationToken.validateToken(accessToken);

            if(validateToken)

            if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
                log.debug("accessToken: "+accessToken);
            }
            else {
                log.debug("No Token");
            }

                // Global PostFilter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.debug("Global PostFilter");
                log.debug("Global Filter - Response status code: {}", response.getStatusCode());
                log.debug("Global Filter - Response headers: {}", response.getHeaders());

            }));
        });
    }

    @Data
    public static class Config{
    }
}
