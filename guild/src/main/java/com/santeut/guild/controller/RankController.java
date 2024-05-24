package com.santeut.guild.controller;

import com.santeut.guild.common.util.ResponseUtil;
import com.santeut.guild.service.RankService;
import com.santeut.guild.service.implementation.RankServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    @GetMapping("/{guildId}/rank/{type}")
    public ResponseEntity<?> getRank(@PathVariable("guildId") int guildId,@PathVariable("type") char type){
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,rankService.getRankList(3,guildId,type));
    }


}
