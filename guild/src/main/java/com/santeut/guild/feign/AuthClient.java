package com.santeut.guild.feign;

import com.santeut.guild.feign.dto.CommentFeignDto;
import com.santeut.guild.feign.dto.UserInfoFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(name = "authClient", url="${user-service.url}")
public interface AuthClient {

    // 유저 정보 가져오기
    @GetMapping("/user/{userId}")
    Optional<FeignResponseDto<UserInfoFeignDto>> getUserInfo(@PathVariable("userId") int userId);

}
