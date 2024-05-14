package com.santeut.hiking.feign;

import com.santeut.hiking.common.config.FeignConfiguration;
import com.santeut.hiking.common.response.BasicResponse;
import com.santeut.hiking.dto.request.HikingTrackSaveReignRequestDto;
import com.santeut.hiking.dto.response.GetUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "HikingAuthClient", url="${auth-service.url}", configuration = FeignConfiguration.class)
public interface HikingAuthClient {
    @GetMapping("/user/{userId}")
    Optional<FeignResponseDto<GetUserInfoResponse>> userInfo(@PathVariable int userId);

}
