package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.util.ResponseUtil;
import com.santeut.community.dto.request.PostCreateRequestDto;
import com.santeut.community.dto.request.PostUpdateRequestDto;
import com.santeut.community.dto.response.PostListResponseDto;
import com.santeut.community.dto.response.PostReadResponseDto;
import com.santeut.community.feign.UserInfoClient;
import com.santeut.community.repository.PostRepository;
import com.santeut.community.service.ImageService;
import com.santeut.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ImageService imageService;

    // 게시글 목록을 불러옴
    @GetMapping("")
    public ResponseEntity<BasicResponse> getPosts(@RequestParam("postType") char postType, @RequestParam(required = false, defaultValue = "2147483647") int lastSeenId) {
        PostListResponseDto result = postService.getPosts(postType, lastSeenId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }

    // 게시글 생성 컨트롤러 ( CREATE )
    @PostMapping("")
    public ResponseEntity<BasicResponse> createPost(@RequestPart(required = false) List<MultipartFile> images,  @RequestPart PostCreateRequestDto postCreateRequestDto, @RequestHeader int userId) {
        postService.createPost(postCreateRequestDto,images, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "성공적으로 게시글 작성");
    }

    // 게시글 디테일 불러옴 ( READ )
    @GetMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> readPost(@PathVariable int postId, @PathVariable char postType, @RequestHeader int userId) {
        PostReadResponseDto postReadResponseDto = postService.readPost(postId, postType, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, postReadResponseDto);
    }

    // 게시글 수정하기 ( UPDATE )
    @PatchMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> updatePost(
            @RequestBody PostUpdateRequestDto postUpdateRequestDto,
            @PathVariable int postId, @PathVariable char postType,
            @RequestHeader int userId
    ) {
        postService.updatePost(postUpdateRequestDto, postId, postType, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "게시글 수정하기를 성공했습니다");
    }

    // 게시글 삭제하기 ( DELETE )
    @DeleteMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> deletePost(@PathVariable int postId, @PathVariable char postType, @RequestHeader int userId) {
        postService.deletePost(postId, postType, userId); // toFix : 3번 째 파라미터 헤더에 담긴 userId로 변환해야함!!!
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "게시글 삭제하기를 성공했습니다.");
    }

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<BasicResponse> uploadFile(@RequestParam("image") MultipartFile image) {
        String url = imageService.uploadImage(image);
        Map<String, String> res = new HashMap<>();
        res.put("imageUrl", url);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, res );
    }
}
