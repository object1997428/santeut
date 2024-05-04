package com.santeut.hiking.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String token = JwtTokenFilter.getToken();
        if (token != null) {
            template.header("Authorization", token);
        }

    }
}
