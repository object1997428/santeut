package com.santeut.auth.domain.dto.requestDto;

import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String userLoginId;
    private String userPassword;
}
