package com.santeut.common.controller;

import com.santeut.common.common.util.ResponseUtil;
import com.santeut.common.dto.request.SaveTokenRequestDto;
import com.santeut.common.service.AlarmTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/alarm/token")
@RequiredArgsConstructor
public class AlarmTokenController {

    private final AlarmTokenService alarmTokenService;

    @PostMapping
    public ResponseEntity<?> saveToken(@RequestBody SaveTokenRequestDto saveTokenRequestDto, HttpServletRequest request){
        String userId = request.getHeader("userId");
        log.info("UserId: "+ userId);
        alarmTokenService.saveFcmToken(Integer.parseInt(userId),saveTokenRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"해당 토큰 저장에 성공했습니다.");
    }
}
