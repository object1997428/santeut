package com.santeut.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/service")
public class AuthServiceController {

    // FilterConfig에서 header를 auth-request로 설정해놨음
    @GetMapping("/test")
    public String message(@RequestHeader("auth-request") String header){

        log.info(header);
        return "MESSAGE";
    }
}
