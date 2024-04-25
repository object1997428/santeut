package com.santeut.auth.Auth.controller;

import com.santeut.auth.Auth.dto.requestDto.SignInRequestDto;
import com.santeut.auth.Auth.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.Auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto dto){

        log.debug("회원가입 : "+ dto.getUserLoginId());
        authService.signUp(dto);
        return ResponseEntity.ok().body("회원가입 성공");

    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto dto){

        log.debug("로그인 : " + dto.getUserLoginId());
        return ResponseEntity.ok().body(authService.signIn(dto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request){

        log.debug("Access Token 재발급: "+ request);
        return ResponseEntity.ok().body(authService.reissueToken(request));
    }

}
