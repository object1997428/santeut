package com.santeut.guild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GuildApplication {

  public static void main(String[] args) {
    SpringApplication.run(GuildApplication.class, args);
  }

}
