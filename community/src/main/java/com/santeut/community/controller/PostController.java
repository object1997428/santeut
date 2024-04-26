package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.util.ResponseUtil;
import com.santeut.community.dto.request.PostRequestDto;
import com.santeut.community.dto.response.PostResponseDto;
import com.santeut.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/post/{postType}")
    public ResponseEntity<BasicResponse> getPosts(@PathVariable char postType) {

        PostResponseDto result = postService.getPosts(postType);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }

    @PostMapping("/post")
    public ResponseEntity<BasicResponse> createPost(@RequestBody PostRequestDto postRequestDto) {
        postService.createPost(postRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "성공적으로 게시글 작성");
    }
}
