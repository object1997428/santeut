package com.santeut.guild.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoFeignDto {
    private int userId;
    private String userNickname;
}
