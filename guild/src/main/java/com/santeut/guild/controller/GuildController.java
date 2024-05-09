package com.santeut.guild.controller;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.GetGuildListResponse;
import com.santeut.guild.service.GuildService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class GuildController {

    private final GuildService guildService;

    @PostMapping("/create")
    public BasicResponse createGuild(@RequestPart("request") CreateGuildRequest request,
                                     @RequestPart(value = "guildProfile", required = false) MultipartFile multipartFile,
                                     HttpServletRequest httpServletRequest){

        log.debug("동호회 생성 / + 이미지 : "+ multipartFile);
        guildService.createGuild(request, httpServletRequest.getHeader("userId"), multipartFile);
        return new BasicResponse(HttpStatus.OK.value(), "동호회 생성 성공");
    }

    @GetMapping("/{guildId}")
    public BasicResponse getDetailGuild(@PathVariable int guildId,  HttpServletRequest request){

        log.debug("userId : "+ request.getHeader("userId"));
        log.debug("동호회 상세 조회 : "+ guildId);
        int userId = Integer.parseInt(request.getHeader("userId"));
        return new BasicResponse(HttpStatus.OK.value(), guildService.getDetailGuild(guildId, userId));
    }

    @PatchMapping("/{guildId}")
    public BasicResponse patchGuild(@PathVariable int guildId,
                                        @RequestPart(value = "request", required = false) PatchGuildInfoRequest request,
                                        @RequestPart(value = "guildProfile", required = false) MultipartFile multipartFile,
                                        HttpServletRequest httpServletRequest){

        String userId = httpServletRequest.getHeader("userId");
        log.debug("동호회 정보 수정 : "+ guildId);
        guildService.patchGuild(guildId, request, multipartFile, userId);
        return new BasicResponse(HttpStatus.OK.value(), "동호회 수정 성공");
    }

    @DeleteMapping("/{guildId}")
    public BasicResponse deleteGuild(@PathVariable int guildId,
                                        HttpServletRequest httpServletRequest){

        String userId = httpServletRequest.getHeader("userId");
        log.debug("동호회 삭제 : "+ guildId);
        guildService.deleteGuild(guildId, userId);
        return new BasicResponse(HttpStatus.OK.value(), "동호회 삭제 성공");
    }

    @GetMapping("/list")
    public BasicResponse getGuildList(){

        log.debug("동호회 목록 조회");
        return new BasicResponse(HttpStatus.OK.value(), guildService.getGuildList());
    }

    @GetMapping("/myguild")
    public BasicResponse myGuildList(HttpServletRequest httpServletRequest){

        log.debug("내 동호회 목록 조회");
        return new BasicResponse(HttpStatus.OK.value(), guildService.myGuildList(httpServletRequest.getHeader("userId")));
    }

    @GetMapping("/search")
    public BasicResponse searchGuild(@RequestParam String regionName,
                                     @RequestParam String gender){

        log.debug("동호회 필터링 검색");
        return new BasicResponse(HttpStatus.OK.value(), guildService.searchGuildList(regionName, gender));
    }
}
