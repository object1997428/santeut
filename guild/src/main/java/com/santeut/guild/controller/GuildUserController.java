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
    public BasicResponse applyGuildList(@PathVariable int guildId, HttpServletRequest request){
        
        log.debug("동호회 가입 신청 리스트");
        return new BasicResponse(HttpStatus.OK.value(), guildUserService.applyGuildList(guildId, request.getHeader("userId")));
    }

    @PatchMapping("/{guildId}/{userId}/approve")
    public BasicResponse approveApply(@PathVariable int guildId,
                                      @PathVariable int userId,
                                      HttpServletRequest request){

        log.debug("가입 신청 승인");
        String leaderUserId = request.getHeader("userId");
        guildUserService.approveApply(guildId, userId, leaderUserId);
        return new BasicResponse(HttpStatus.OK.value(), "가입 신청 승인 성공");
    }

    @PatchMapping("/{guildId}/{userId}/deny")
    public BasicResponse denyApply(@PathVariable int guildId,
                                      @PathVariable int userId,
                                   HttpServletRequest request){

        log.debug("가입 신청 거부");
        String leaderUserId = request.getHeader("userId");
        guildUserService.denyApply(guildId, userId, leaderUserId);
        return new BasicResponse(HttpStatus.OK.value(), "가입 신청 거부 성공");
    }

    @GetMapping("/{guildId}/member-list")
    public BasicResponse guildMemberList(@PathVariable int guildId){

        log.debug("동호회 회원 목록 조회");
        return new BasicResponse(HttpStatus.OK.value(), guildUserService.memberList(guildId));
    }

    @DeleteMapping("/{guildId}/member-list/{userId}")
    public BasicResponse kickMember(@PathVariable int guildId,
                                    @PathVariable int userId,
                                    HttpServletRequest request){

        log.debug("동호회 회원 추방");
        String leaderUserId = request.getHeader("userId");
        guildUserService.kickMember(guildId, userId, leaderUserId);
        return new BasicResponse(HttpStatus.OK.value(), "동호회 회원 추방 성공");
    }

    @PatchMapping("/{guildId}/delegate/{newLeaderId}")
    public BasicResponse delegateLeader(@PathVariable int guildId,
                                        @PathVariable int newLeaderId,
                                        HttpServletRequest request){

        log.debug("동호회장 위임");
        int oldLeaderId = Integer.parseInt(request.getHeader("userId"));
        guildUserService.delegateLeader(guildId, newLeaderId, oldLeaderId);
        return new BasicResponse(HttpStatus.OK.value(), "동호회장 위임 성공");
    }

    @DeleteMapping("/{guildId}/quit")
    public BasicResponse quitGuild(@PathVariable int guildId,
                                   HttpServletRequest request){

        log.debug("동호회 탈퇴");
        int userId = Integer.parseInt(request.getHeader("userId"));
        guildUserService.quitGuild(guildId, userId);
        return new BasicResponse(HttpStatus.OK.value(), "동호회 탈퇴 성공");
    }
}
