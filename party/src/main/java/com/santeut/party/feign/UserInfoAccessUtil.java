package com.santeut.party.feign;

import com.santeut.party.feign.dto.UserInfoFeignResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoAccessUtil {

  private final UserInfoClient userInfoClient;

  public UserInfoFeignResponseDto getLoginUserInfo(int userId) {
    UserInfoFeignResponseDto fromUser = userInfoClient.getLoginUserInfo(userId).getData();
    return fromUser;
  }

  public UserInfoFeignResponseDto getUserInfo(int userId) {
    UserInfoFeignResponseDto fromUser = userInfoClient.getUserInfo(userId).getData();
    return fromUser;

  }


}
