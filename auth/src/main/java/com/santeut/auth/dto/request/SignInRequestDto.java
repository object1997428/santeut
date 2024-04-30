package com.santeut.auth.dto.request;

import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String userLoginId;
    private String userPassword;
}
