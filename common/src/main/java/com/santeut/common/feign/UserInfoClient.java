package com.santeut.common.feign;

import com.santeut.common.common.config.FeignConfiguration;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="userInfoClient", url = "${user-service.url}", configuration = FeignConfiguration.class)
public interface UserInfoClient {
    @GetMapping("/user")
    Optional<UserInfoFeignRequestDto> getUserInfo();
    @GetMapping("/user/{userId}")
    Optional<UserInfoFeignRequestDto> getUserInfo(@PathVariable("userId") int userId);
}
