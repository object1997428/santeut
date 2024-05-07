package com.santeut.guild.controller;


import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.common.util.ResponseUtil;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post")
@Slf4j
@RestController
@RequiredArgsConstructor
public class GuildPostController {

    private final PostService postService;

    // 길드 게시글 작성하기 (CREATE)
    @PostMapping("")
    public ResponseEntity<BasicResponse> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, @RequestHeader int userId ) {
        postService.createPost(postCreateRequestDto, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 길드 게시글 작성");
    }

    // 길드 게시글 목록보기 (READ)
    @GetMapping("/{guildId}/{categoryId}")
    public ResponseEntity<BasicResponse> getPosts(@PathVariable int guildId,
                                                  @PathVariable int categoryId,
                                                  @RequestParam(required = false, defaultValue = "2147483647") int lastSeenId
    ) {
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postService.getPosts(guildId, categoryId, lastSeenId));
    }
}
