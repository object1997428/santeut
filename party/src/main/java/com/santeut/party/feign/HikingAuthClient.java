package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.dto.request.HikingRecordUpdateFeignRequest;
import com.santeut.party.dto.request.MountainCourseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "HikingAuthClient", url="${auth-service.url}", configuration = FeignConfiguration.class)
public interface HikingAuthClient {
    @PatchMapping("/user/record")
    ResponseEntity<?> updateHikingRecord(@RequestBody HikingRecordUpdateFeignRequest hikingRecordUpdateFeignRequest);
}
