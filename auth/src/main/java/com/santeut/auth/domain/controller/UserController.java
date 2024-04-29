package com.santeut.auth.domain.controller;

import com.santeut.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

//    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("UserLoginId: "+ userDetails.getUsername());
        return ResponseEntity.ok(userDetails.getUsername());
    }
}
