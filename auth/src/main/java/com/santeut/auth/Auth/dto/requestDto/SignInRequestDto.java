package com.santeut.auth.Auth.dto.requestDto;

import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String userLoginId;
    private String userPassword;
}
