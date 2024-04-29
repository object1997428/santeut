package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.util.ResponseUtil;
import com.santeut.community.dto.request.PostReqeustRequestDto;
import com.santeut.community.dto.response.PostListResponseDto;
import com.santeut.community.dto.response.PostReadResponseDto;
import com.santeut.community.feign.UserInfoClient;
import com.santeut.community.repository.PostRepository;
import com.santeut.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserInfoClient userInfoClient;
    private final PostRepository postRepository;

    // 게시글 목록을 불러옴
    @GetMapping("/post")
    public ResponseEntity<BasicResponse> getPosts(@RequestParam("postType") char postType) {
        PostListResponseDto result = postService.getPosts(postType);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }

    // 게시글 생성 컨트롤러 ( CREATE )
    @PostMapping("/post")
    public ResponseEntity<BasicResponse> createPost(@RequestBody PostReqeustRequestDto postReqeustRequestDto) {
        postService.createPost(postReqeustRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "성공적으로 게시글 작성");
    }

    // 게시글 디테일 불러옴 ( READ )
    @GetMapping("/post/{postId}/{postType}")
    public ResponseEntity<BasicResponse> readPost(@PathVariable int postId, @PathVariable char postType) {
        PostReadResponseDto postReadResponseDto = postService.readPost(postId, postType);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postReadResponseDto);
    }

    // 게시글 수정하기 ( UPDATE )
    @PatchMapping("/post/{postId}")
    public ResponseEntity<BasicResponse> updatePost() {
        return null;
    }

    // 게시글 삭제하기 ( DELETE )
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<BasicResponse> deletePost() {
        return null;
    }

    // 임시 openFeign 테스트
    @GetMapping("/openfeign")
    public ResponseEntity<BasicResponse> openFeign() {
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, userInfoClient.getUserInfo());
    }
}
