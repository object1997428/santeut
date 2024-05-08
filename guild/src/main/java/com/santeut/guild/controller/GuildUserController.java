package com.santeut.guild.controller;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.service.GuildUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class GuildUserController {

    private final GuildUserService guildUserService;

    @PostMapping("/apply/{guildId}")
    public BasicResponse applyGuild(@PathVariable int guildId, HttpServletRequest request){

        log.debug("동호회 가입 신청");
        guildUserService.applyGuild(guildId, request.getHeader("userId"));
        return new BasicResponse(HttpStatus.OK.value(), "동호회 가입 신청 성공");
    }

    @GetMapping("/apply-list/{guildId}")
    private BasicResponse applyGuildList(@PathVariable int guildId){
        
        log.debug("동호회 목록 리스트");
        return new BasicResponse(HttpStatus.OK.value(), guildUserService.applyGuildList(guildId));
    }
}
