package com.santeut.auth.domain.service;


import com.santeut.auth.domain.dto.requestDto.SignInRequestDto;
import com.santeut.auth.domain.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.domain.dto.responseDto.JwtTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void signUp(SignUpRequestDto dto);
    JwtTokenResponseDto signIn(SignInRequestDto dto);
    JwtTokenResponseDto reissueToken(HttpServletRequest request);

}
