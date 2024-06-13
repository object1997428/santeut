package com.santeut.hiking.feign;

import com.santeut.hiking.common.config.FeignConfiguration;
import com.santeut.hiking.common.response.BasicResponse;
import com.santeut.hiking.dto.request.HikingTrackSaveReignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "HikingPartyClient", url="${party-service.url}", configuration = FeignConfiguration.class)
public interface HikingPartyClient {
    @PostMapping("/hiking/track")
    ResponseEntity<?> saveHikingTrack(@RequestBody HikingTrackSaveReignRequestDto hikingTrackSaveReignRequestDto);
    @GetMapping("/{partyId}/userId")
    ResponseEntity<?> getPartyUserId(@PathVariable("partyId") int partyId);
}
