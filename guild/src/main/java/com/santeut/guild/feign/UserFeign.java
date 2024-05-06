package com.santeut.guild.feign;

import com.santeut.guild.common.response.BasicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "userFeign", url="https://k10e201.p.ssafy.io:52711/api")
public interface UserFeign {

    @GetMapping(path = "/login/ifno")
    public BasicResponse userLoginInfo();
}
