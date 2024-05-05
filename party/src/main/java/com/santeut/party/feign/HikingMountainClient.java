package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.dto.request.MountainCourseRequest;
import com.santeut.party.dto.response.PartyTrackDataReginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "HikingMountainClient", url="${mountain-service.url}", configuration = FeignConfiguration.class)
public interface HikingMountainClient {
    @PostMapping("/course/coordinates")
    ResponseEntity<?> getCourse(@RequestBody MountainCourseRequest mountainCourseRequest);
}
