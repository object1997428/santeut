package com.santeut.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomRoute {

  @Bean
  public RouteLocator cRoute(RouteLocatorBuilder builder) {

    return builder.routes()
        .route("auth", r -> r.path("/auth/**")
            .uri("http://localhost:8081"))
        .route("mountain", r -> r.path("/mountain/**")
            .uri("http://localhost:8082"))
        .route("guild", r -> r.path("/guild/**")
            .uri("http://localhost:8083"))
        .route("party", r -> r.path("/party/**")
            .uri("http://localhost:8084"))
        .route("hiking", r -> r.path("/hiking/**")
            .uri("http://localhost:8085"))
        .route("community", r -> r.path("/community/**")
            .uri("http://localhost:8086"))

        .build();
  }

}
