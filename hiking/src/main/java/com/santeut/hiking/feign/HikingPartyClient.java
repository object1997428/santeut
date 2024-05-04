package com.santeut.hiking.feign;

import com.santeut.hiking.common.config.FeignConfiguration;
import com.santeut.hiking.dto.request.HikingTrackSaveReignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "HikingStartClient", url="${party-service.url}", configuration = FeignConfiguration.class)
public interface HikingPartyClient {
    @PostMapping("/hiking/track")
    ResponseEntity<?> saveHikingTrack(@RequestBody HikingTrackSaveReignRequestDto hikingTrackSaveReignRequestDto);
}
