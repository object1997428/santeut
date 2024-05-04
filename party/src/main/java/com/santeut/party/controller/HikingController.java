package com.santeut.party.controller;

import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.request.HikingTrackSaveReignRequestDto;
import com.santeut.party.entity.HikingEnterRequest;
import com.santeut.party.service.HikingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HikingController {
    private final HikingService hikingService;

    @PostMapping("/hiking/start")
    public ResponseEntity<?> startHiking(@RequestBody HikingEnterRequest hikingEnterRequest){
        hikingService.hikingStart(hikingEnterRequest);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"등산 시작 처리에 성공했습니다.");
    }

    @PostMapping("/hiking/track")
    public ResponseEntity<?> saveHikingTrack(@RequestBody HikingTrackSaveReignRequestDto hikingTrackSaveReignRequestDto){
        log.info("Hiking Server 요청 들어옴");
        hikingService.saveTrack(hikingTrackSaveReignRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"좌표 저장에 성공했습니다.");
    }
}
