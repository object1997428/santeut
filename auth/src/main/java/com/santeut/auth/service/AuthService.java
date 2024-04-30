package com.santeut.auth.service;


import com.santeut.auth.dto.request.SignInRequestDto;
import com.santeut.auth.dto.request.SignUpRequestDto;
import com.santeut.auth.dto.response.JwtTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void signUp(SignUpRequestDto dto);
    JwtTokenResponseDto signIn(SignInRequestDto dto);
    JwtTokenResponseDto reissueToken(HttpServletRequest request);

}
