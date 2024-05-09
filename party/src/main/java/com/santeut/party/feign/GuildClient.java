package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.feign.dto.response.FeignResponseDto;
import com.santeut.party.feign.dto.response.GuildInfoFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "guildClient", url = "${guild-service.url}", configuration = FeignConfiguration.class)
public interface GuildClient {

  // 동호회 정보 조회
  @GetMapping("/{guildId}")
  FeignResponseDto<GuildInfoFeignResponseDto> getGuildInfo(@PathVariable("guildId") int guildId);

}
