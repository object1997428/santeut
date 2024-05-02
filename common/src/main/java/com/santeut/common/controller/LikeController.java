package com.santeut.common.controller;

import com.santeut.common.common.response.BasicResponse;
import com.santeut.common.common.util.ResponseUtil;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.service.CommentService;
import com.santeut.common.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/like")
@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 개수 가져오기 쓰기 (READ)
    @GetMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType) {
        Map<String, Integer> result = new HashMap<>();
        int cnt = likeService.getLikeCnt(postId, postType);
        result.put("likeCnt",cnt );
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }
}
