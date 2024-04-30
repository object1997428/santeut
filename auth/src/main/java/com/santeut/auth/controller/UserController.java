package com.santeut.auth.controller;

import com.santeut.auth.common.response.BasicResponse;
import com.santeut.auth.dto.request.UpdatePasswordRequest;
import com.santeut.auth.dto.request.UpdateProfileRequest;
import com.santeut.auth.service.UserService;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login/info")
    public BasicResponse userLoginInfo(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("UserLoginId: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.userLoginInfo(userDetails.getUsername()));
    }

    @GetMapping("/{userId}")
    public BasicResponse userInfo(@PathVariable int userId){

        log.debug("userId: "+ userId);
        // status, Data
        return new BasicResponse(HttpStatus.OK.value(), userService.userInfo(userId));
    }

    @PutMapping("/password")
    public BasicResponse updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody UpdatePasswordRequest request){

        log.debug("Password 수정하는 유저 ID: "+ userDetails.getUsername());
        userService.updatePassword(userDetails.getUsername(), request);
        return new BasicResponse(HttpStatus.OK.value(), null);
    }

    @PatchMapping("/profile")
    public BasicResponse updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody UpdateProfileRequest request){

        log.debug("Password 수정하는 유저 ID: "+ userDetails.getUsername());
        userService.updateProfile(userDetails.getUsername(), request);
        return new BasicResponse(HttpStatus.OK.value(), request.getUserProfile());
    }

    @GetMapping("/level")
    public BasicResponse getLevel(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("등급 조회 ID: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.getLevel(userDetails.getUsername()));
    }

    @GetMapping("/record")
    private BasicResponse gerRecord(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("등산 기록 조회 ID: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.getMountainRecord(userDetails.getUsername()));
    }
}
