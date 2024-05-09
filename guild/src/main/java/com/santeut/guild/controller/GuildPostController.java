package com.santeut.guild.controller;


import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.common.util.ResponseUtil;
import com.santeut.guild.dto.request.GuildPostUpdateRequestDto;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.dto.response.PostListResponseDto;
import com.santeut.guild.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/post")
@Slf4j
@RestController
@RequiredArgsConstructor
public class GuildPostController {

    private final PostService postService;

    // 길드 게시글 작성하기 (CREATE)
    @PostMapping("")
    public ResponseEntity<BasicResponse> createPost(@RequestPart(required = false) List<MultipartFile> images, @RequestPart PostCreateRequestDto postCreateRequestDto, @RequestHeader int userId ) {
        postService.createPost(postCreateRequestDto,images, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 길드 게시글 작성");
    }

    // 길드 게시글 목록보기 (READ)
    @GetMapping("/{guildId}/{categoryId}")
    public ResponseEntity<BasicResponse> getPosts(@PathVariable int guildId,
                                                  @PathVariable int categoryId,
                                                  @RequestParam(required = false, defaultValue = "2147483647") int lastSeenId
    ) {
//        List<PostListResponseDto> postList = postService.getPosts(guildId, categoryId, lastSeenId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postService.getPosts(guildId, categoryId, lastSeenId));
    }

    // 길드 게시글 디테일 보기 (READ)
    @GetMapping("/{guildPostId}")
    public ResponseEntity<BasicResponse> readPost(@PathVariable int guildPostId, @RequestHeader int userId ) {
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postService.readPost(guildPostId, userId));
    }

    // 게시글 삭제하기 ( DELETE )
    @DeleteMapping("/{guildPostId}")
    public ResponseEntity<BasicResponse> deletePost(@PathVariable int guildPostId, @RequestHeader int userId) {
        postService.deletePost(guildPostId, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "게시글 삭제하기를 성공했습니다.");
    }

    // 게시글 수정하기 ( UPDATE )
    @PatchMapping("/{guildPostId}")
    public ResponseEntity<BasicResponse> deletePost(@RequestBody GuildPostUpdateRequestDto guildPostUpdateRequestDto,@PathVariable int guildPostId, @RequestHeader int userId) {
        postService.updatePost(guildPostUpdateRequestDto, guildPostId, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 게시글을 수정하였습니다");
    }
}
