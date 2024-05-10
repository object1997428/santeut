package com.santeut.hiking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
//@EnableWebSocketMessageBroker
@SpringBootApplication
public class HikingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HikingApplication.class, args);
    }

}
