package com.santeut.party.feign;

import com.santeut.party.feign.dto.response.GuildInfoFeignResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildAccessUtil {

  private final GuildClient guildClient;

  public GuildInfoFeignResponseDto getGuildInfo(int guildId) {
    return guildClient.getGuildInfo(guildId).getData();
  }

}
