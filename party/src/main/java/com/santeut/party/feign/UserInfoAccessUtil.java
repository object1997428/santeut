package com.santeut.party.feign;

import com.santeut.party.common.exception.FeignException;
import com.santeut.party.feign.dto.response.FeignResponseDto;
import com.santeut.party.feign.dto.response.UserInfoFeignResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoAccessUtil {

  private final UserInfoClient userInfoClient;

  public UserInfoFeignResponseDto getLoginUserInfo(int userId) {
    FeignResponseDto<UserInfoFeignResponseDto> response = userInfoClient.getLoginUserInfo(userId);
    if (response.getStatus() != 200) {
      log.error("[party->auth] 로그인 유저 정보를 불러오는 데 실패했습니다/userId: "+userId);
      throw new FeignException("[party->auth] 로그인 유저 정보를 불러오는 데 실패했습니다");
    }
    return response.getData();
  }

  public UserInfoFeignResponseDto getUserInfo(int userId) {
    FeignResponseDto<UserInfoFeignResponseDto> response = userInfoClient.getUserInfo(userId);
    if(response.getStatus() != 200) {
      log.error("[party->auth] userId로 유저 정보를 불러오는 데 실패했습니다/userId: "+userId);
      throw new FeignException("[party->auth] userId로 유저 정보를 불러오는 데 실패했습니다");
    }
    return response.getData();
  }


}
