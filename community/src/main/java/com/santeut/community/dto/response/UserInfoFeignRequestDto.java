package com.santeut.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoFeignRequestDto {
    private int userId;
    private String userNickname;
}
