package com.santeut.community.feign;

import com.santeut.community.common.config.FeignConfiguration;
import com.santeut.community.dto.response.UserInfoFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userInfoClient", url="https://https://k10e201.p.ssafy.io/api", configuration = FeignConfiguration.class)
public interface UserInfoClient {
    @GetMapping("/")
    UserInfoFeignRequestDto getUserInfo();
}
