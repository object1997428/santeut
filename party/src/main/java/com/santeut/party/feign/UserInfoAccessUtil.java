package com.santeut.party.feign;

import com.santeut.party.common.exception.FeignException;
import com.santeut.party.feign.dto.request.GetPartyMemberInfoRequest;
import com.santeut.party.feign.dto.response.FeignResponseDto;
import com.santeut.party.feign.dto.response.GetPartyMemberInfoResponse;
import com.santeut.party.feign.dto.response.GetPartyMemberInfoResponse.PartyMemberInfo;
import com.santeut.party.feign.dto.response.UserInfoFeignResponseDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

  public GetPartyMemberInfoResponse getPartyMemberInfo(int partyId, int userId, List<Integer> memberIdList) {
    ResponseEntity<FeignResponseDto<GetPartyMemberInfoResponse>> memberResp = userInfoClient.getPartyMemberInfo(userId,
        new GetPartyMemberInfoRequest(partyId, memberIdList));
    if(!memberResp.getStatusCode().is2xxSuccessful()) {
      log.error("[party->auth] 소모임 유저 정보를 불러오는 데 실패했습니다/userId {}",userId);
      throw new FeignException("[party->auth] 소모임 유저 정보를 불러오는 데 실패했습니다");
    }
    return Objects.requireNonNull(memberResp.getBody()).getData();
  }


}
