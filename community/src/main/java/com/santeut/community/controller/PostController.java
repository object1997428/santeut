package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.util.ResponseUtil;
import com.santeut.community.dto.request.PostCreateReqeustRequestDto;
import com.santeut.community.dto.request.PostUpdateReqeustRequestDto;
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
    public ResponseEntity<BasicResponse> createPost(@RequestBody PostCreateReqeustRequestDto postCreateReqeustRequestDto) {
        postService.createPost(postCreateReqeustRequestDto);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "성공적으로 게시글 작성");
    }

    // 게시글 디테일 불러옴 ( READ )
    @GetMapping("/post/{postId}/{postType}")
    public ResponseEntity<BasicResponse> readPost(@PathVariable int postId, @PathVariable char postType) {
        PostReadResponseDto postReadResponseDto = postService.readPost(postId, postType);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postReadResponseDto);
    }

    // 게시글 수정하기 ( UPDATE )
    @PatchMapping("/post/{postId}/{postType}")
    public ResponseEntity<BasicResponse> updatePost(@RequestBody PostUpdateReqeustRequestDto postUpdateReqeustRequestDto, @PathVariable int postId, @PathVariable char postType) {
        postService.updatePost(postUpdateReqeustRequestDto, postId, postType);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "게시글 수정하기를 성공했습니다");
    }

    // 게시글 삭제하기 ( DELETE )
    @DeleteMapping("/post/{postId}/{postType}")
    public ResponseEntity<BasicResponse> deletePost(@PathVariable int postId, @PathVariable char postType) {
        postService.deletePost(postId, postType, 1); // toFix : 3번 째 파라미터 헤더에 담긴 userId로 변환해야함!!!
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "게시글 삭제하기를 성공했습니다.");
    }
}
