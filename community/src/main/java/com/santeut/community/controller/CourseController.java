package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.util.ResponseUtil;
import com.santeut.community.dto.request.PostCreateRequestDto;
import com.santeut.community.dto.request.PostUpdateRequestDto;
import com.santeut.community.dto.response.CourseReadResponseDto;
import com.santeut.community.dto.response.PostListResponseDto;
import com.santeut.community.dto.response.PostReadResponseDto;
import com.santeut.community.service.CourseService;
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
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    // 코스게시글 생성 컨트롤러 ( CREATE )
    @PostMapping("")
    public ResponseEntity<BasicResponse> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, @RequestHeader int userId) {
        courseService.createPost(postCreateRequestDto, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "성공적으로 게시글 작성");
    }

    // 코스게시글 조회 ( READ )
    @GetMapping("/{postId}/{postType}/{partyUserId}")
    public ResponseEntity<BasicResponse> readPost(@PathVariable int postId, @PathVariable char postType, @PathVariable int partyUserId, @RequestHeader int userId) {
        CourseReadResponseDto courseReadResponseDto = courseService.readPost(postId, postType, partyUserId, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, courseReadResponseDto);
    }
}
