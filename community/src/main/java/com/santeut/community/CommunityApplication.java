package com.santeut.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
public class CommunityApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommunityApplication.class, args);
  }

}
