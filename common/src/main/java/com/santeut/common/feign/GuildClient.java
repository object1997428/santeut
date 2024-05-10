package com.santeut.common.feign;

import com.santeut.common.common.config.FeignConfiguration;
import com.santeut.common.dto.request.CommunityFeignDto;
import com.santeut.common.dto.request.GuildPostFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name="GuildClient", url = "${guild-service.url}", configuration = FeignConfiguration.class)
public interface GuildClient {
    @GetMapping(value = "/post/{guildPostId}")
    Optional<FeignResponseDto<GuildPostFeignDto>> getPostInfo(@PathVariable int guildPostId, @RequestHeader int userId);
}
