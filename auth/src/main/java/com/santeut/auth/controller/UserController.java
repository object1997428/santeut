package com.santeut.auth.controller;

import com.santeut.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login/info")
    public ResponseEntity<?> userLoginInfo(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("UserLoginId: "+ userDetails.getUsername());
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @GetMapping("/{userid}")
    public ResponseEntity<?> userInfo(@PathVariable int userid){

        log.debug("userId: "+ userid);
//        userService.userInfo();
        return ResponseEntity.ok(userService.userInfo(userid));
    }
}
