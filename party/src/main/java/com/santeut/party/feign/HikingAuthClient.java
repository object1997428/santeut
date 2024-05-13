package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.feign.dto.request.HikingRecordRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "HikingAuthClient", url="${auth-service.url}", configuration = FeignConfiguration.class)
public interface HikingAuthClient {
    @PatchMapping("/user/record")
    ResponseEntity<?> patchRecord(@RequestBody HikingRecordRequest request);
}
