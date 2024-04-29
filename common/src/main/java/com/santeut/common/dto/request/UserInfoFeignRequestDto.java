package com.santeut.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoFeignRequestDto {
    private int userId;
    private String userNickname;
}

