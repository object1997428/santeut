package com.santeut.community.feign;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final ThreadLocal<String> jwtTokenHolder = new ThreadLocal<>();

    public static String getToken() {
        return jwtTokenHolder.get();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        try {
            jwtTokenHolder.set(jwtToken);
            filterChain.doFilter(request, response);
        } finally {
            jwtTokenHolder.remove();
        }
    }
}