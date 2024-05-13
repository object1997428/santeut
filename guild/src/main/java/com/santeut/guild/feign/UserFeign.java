package com.santeut.guild.feign;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.common.util.ResponseUtil;
import com.santeut.guild.dto.response.UserInfoResponse;
import com.santeut.guild.feign.dto.PartyMemberInfoRequest;
import com.santeut.guild.feign.dto.PartyMemberInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userFeign", url="http://k10e201.p.ssafy.io:52711/api")
public interface UserFeign {

    @GetMapping(path = "auth/user/login/info")
    public ResponseEntity<?> userLoginInfo();

    @GetMapping(path = "auth/user/{userId}")
    public BasicResponse userInfo(@PathVariable int userId);

    @PostMapping("auth/user/party/profile")
    ResponseEntity<FeignResponseDto<PartyMemberInfoResponse>> getPartyMemberInfo(
            @RequestBody PartyMemberInfoRequest request
    );


}
