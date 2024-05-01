package com.santeut.party.controller;

import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.entity.HikingEnterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/party")
@Slf4j
public class HikingController {

//    @PostMapping("/hiking/start")
//    public RequestEntity<?> startHiking(@RequestBody HikingEnterRequest hikingEnterRequest){
//        return ResponseUtil.buildBasicResponse(HttpStatus.OK,"")
//    }
}
