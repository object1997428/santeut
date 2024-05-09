package com.santeut.common.controller;

import com.santeut.common.common.response.BasicResponse;
import com.santeut.common.common.util.ResponseUtil;
import com.santeut.common.dto.request.AlarmRequestDto;
import com.santeut.common.dto.request.CommonHikingStartFeignRequest;
import com.santeut.common.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/alarm")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    // 알람 작성 하기 ( CREATE )
    @PostMapping("/{referenceId}/{referenceType}")
    public ResponseEntity<BasicResponse> createAlarm(@PathVariable Integer referenceId, @PathVariable Character referenceType, @RequestBody AlarmRequestDto alarmRequestDto) {
        alarmService.createAlarm(referenceId, referenceType, alarmRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "알람이 성공적으로 생성되었습니다.");
    }

    // 알람 삭제하기 ( DELETE )
    @DeleteMapping("/alarmId")
    public ResponseEntity<BasicResponse> deleteAlarm(@PathVariable Integer alarmId) {
        alarmService.deleteAlarm(alarmId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "알람이 성공적으로 삭제되었습니다.");
    }

    // 알람 목록 조회하기 ( READ )
    @GetMapping("")
    public ResponseEntity<BasicResponse> getAlarms(@RequestHeader int userId) {
        alarmService.getAlarms(userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, alarmService.getAlarms(userId));
    }

    //알림 생성하기
    @PostMapping
    ResponseEntity<?> alertHiking(@RequestBody CommonHikingStartFeignRequest hikingStartFeignRequest){
        alarmService.sendAlarm(hikingStartFeignRequest);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "알람이 성공적으로 생성되었습니다.");
    }

}
