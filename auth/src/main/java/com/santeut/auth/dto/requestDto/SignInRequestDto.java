package com.santeut.auth.dto.requestDto;

import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String userLoginId;
    private String userPassword;
}
