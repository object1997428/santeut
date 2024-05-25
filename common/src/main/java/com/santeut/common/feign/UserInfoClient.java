package com.santeut.common.feign;

import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="userInfoClient", url = "${user-service.url}")
public interface UserInfoClient {
    @GetMapping("/user/{userId}")
    Optional<FeignResponseDto<UserInfoFeignRequestDto>> getUserInfo(@PathVariable("userId") int userId);
}
