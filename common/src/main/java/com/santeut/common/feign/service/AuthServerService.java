package com.santeut.common.feign.service;

import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import com.santeut.common.feign.UserInfoClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class AuthServerService {
    private final UserInfoClient userInfoClient;

    public String getNickname(Integer userId) {
        UserInfoFeignRequestDto userInfoFeignRequestDto = userInfoClient.getUserInfo(userId).orElseThrow(() -> new FeignClientException("닉네임 정보를 불러오는데 실패했습니다.")).getData();
        return userInfoClient.getUserInfo(userId).orElseThrow(() -> new FeignClientException("닉네임 정보를 불러오는데 실패했습니다.")).getData().getUserNickname();
    }
}
