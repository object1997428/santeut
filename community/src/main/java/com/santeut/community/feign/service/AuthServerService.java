package com.santeut.community.feign.service;

import com.santeut.community.common.exception.FeignClientException;
import com.santeut.community.dto.response.UserInfoFeignRequestDto;
import com.santeut.community.feign.UserInfoClient;
import com.santeut.community.feign.UserInfoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServerService {
    private final UserInfoClient userInfoClient;

    public String getNickname(Integer userId) {
        return userInfoClient.getUserInfo(userId).orElseThrow(() -> new FeignClientException("닉네임 정보를 불러오는데 실패했습니다.")).getData().getUserNickname();
    }
    public UserInfoFeignRequestDto getUserInfo(Integer userId) {
        return userInfoClient.getUserInfo().orElseThrow(() -> new FeignClientException("유저 아이디 정보를 불러오는데 실패했습니다."));
    }
}
