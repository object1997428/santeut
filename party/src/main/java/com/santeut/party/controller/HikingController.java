package com.santeut.party.controller;

import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.request.HikingExitRequest;
import com.santeut.party.dto.request.HikingSafetyRequest;
import com.santeut.party.feign.dto.request.HikingTrackSaveFeignRequestDto;
import com.santeut.party.dto.request.HikingEnterRequest;
import com.santeut.party.service.HikingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HikingController {
    private final HikingService hikingService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/hiking/start")
    public ResponseEntity<?> startHiking(@RequestBody HikingEnterRequest hikingEnterRequest, HttpServletRequest request){
        String userId = request.getHeader("userId");
        log.info("userId={}",userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,hikingService.hikingStart(Integer.parseInt(userId),hikingEnterRequest));
    }

    @PostMapping("/hiking/track")
    public ResponseEntity<?> saveHikingTrack(@RequestBody HikingTrackSaveFeignRequestDto hikingTrackSaveFeignRequestDto){
        log.info("Hiking Server 요청 들어옴");
        hikingService.saveTrack(hikingTrackSaveFeignRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"좌표 저장에 성공했습니다.");
    }

    @PostMapping("/hiking/end")
    public ResponseEntity<?> endHiking(@RequestBody HikingExitRequest hikingExitRequest, HttpServletRequest request){
        String userId = request.getHeader("userId");
        log.info("userId={}",userId);
        hikingService.hikingEnd(Integer.parseInt(userId),hikingExitRequest);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"등산 퇴장 처리에 성공했습니다.");
    }

    @PostMapping("/hiking/safety")
    public ResponseEntity<?> safetyAlert(@RequestBody HikingSafetyRequest hikingSafetyRequest, HttpServletRequest request){
        String userId = request.getHeader("userId");
        log.info("userId={}",userId);
        hikingService.hikingSafety(Integer.parseInt(userId),hikingSafetyRequest);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"등산 위험 알림에 성공했습니다.");
    }
}
