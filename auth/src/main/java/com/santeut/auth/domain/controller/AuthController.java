package com.santeut.auth.domain.controller;


import com.santeut.auth.domain.dto.requestDto.SignInRequestDto;
import com.santeut.auth.domain.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.domain.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("welcome")
    public String welcome(){
        return "WELCOME";
    }

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
