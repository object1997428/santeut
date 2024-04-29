package com.santeut.auth.service;


import com.santeut.auth.dto.requestDto.SignInRequestDto;
import com.santeut.auth.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.dto.responseDto.JwtTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void signUp(SignUpRequestDto dto);
    JwtTokenResponseDto signIn(SignInRequestDto dto);
    JwtTokenResponseDto reissueToken(HttpServletRequest request);

}
