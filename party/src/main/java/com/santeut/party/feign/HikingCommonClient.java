package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.feign.dto.request.CommonHikingStartFeignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "HikingCommonClient", url="${common-service.url}", configuration = FeignConfiguration.class)
public interface HikingCommonClient {
    @PostMapping("/alarm")
    ResponseEntity<?> alertHiking(@RequestBody CommonHikingStartFeignRequest hikingStartFeignRequest);

}
