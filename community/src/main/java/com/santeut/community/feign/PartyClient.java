package com.santeut.community.feign;

import com.santeut.community.feign.dto.FeignPartyLatLngResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "PartyClient", url="${party-service.url}")
public interface PartyClient {

    // 유저-소모임 좌표 리스트 불러오기
    @GetMapping("/user/{partyUserId}")
    Optional<FeignResponseDto<FeignPartyLatLngResponseDto>> getLatLngList(@PathVariable Integer partyUserId, @RequestHeader int userId);

}
