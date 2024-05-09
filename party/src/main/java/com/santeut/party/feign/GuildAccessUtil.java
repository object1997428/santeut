package com.santeut.party.feign;

import com.santeut.party.common.exception.FeignException;
import com.santeut.party.feign.dto.response.FeignResponseDto;
import com.santeut.party.feign.dto.response.GuildInfoFeignResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildAccessUtil {

  private final GuildClient guildClient;

  public GuildInfoFeignResponseDto getGuildInfo(int guildId) {
    FeignResponseDto<GuildInfoFeignResponseDto> response = guildClient.getGuildInfo(guildId);
    if(response.getStatus() != 200) {
      log.error("[party->guild] 동호회 정보를 불러오는 데 실패했습니다/guildId: "+guildId);
      throw new FeignException("[party->guild] 동호회 정보를 불러오는 데 실패했습니다");
    }

    return response.getData();
  }

}
