package com.santeut.guild.feign;

import com.santeut.guild.common.response.BasicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "userFeign", url="http://k10e201.p.ssafy.io:52711/api")
public interface UserFeign {

    @GetMapping(path = "authg/user/login/info")
    public BasicResponse userLoginInfo();

    @GetMapping(path = "auth/user/{userId}")
    public BasicResponse userInfo(@PathVariable int userId);
}
