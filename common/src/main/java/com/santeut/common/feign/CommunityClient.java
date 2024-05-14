package com.santeut.common.feign;

import com.santeut.common.common.config.FeignConfiguration;
import com.santeut.common.dto.request.CommunityFeignDto;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(name="CommunityClient", url = "${community-service.url}", configuration = FeignConfiguration.class)
public interface CommunityClient {
    @GetMapping(value = "/post/{postId}/{postType}")
    Optional<FeignResponseDto<CommunityFeignDto>> getPostInfo(@PathVariable int postId, @PathVariable char postType, @RequestHeader int userId);
}
