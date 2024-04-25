package com.santeut.auth.Auth.service;


import com.santeut.auth.Auth.dto.requestDto.SignInRequestDto;
import com.santeut.auth.Auth.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.Auth.dto.responseDto.JwtTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void signUp(SignUpRequestDto dto);
    JwtTokenResponseDto signIn(SignInRequestDto dto);
    JwtTokenResponseDto reissueToken(HttpServletRequest request);

}
