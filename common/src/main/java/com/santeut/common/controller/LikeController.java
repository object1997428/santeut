package com.santeut.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.santeut.common.common.response.BasicResponse;
import com.santeut.common.common.util.ResponseUtil;
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
    @GetMapping("/cnt/{postId}/{postType}")
    public ResponseEntity<BasicResponse> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType) {
        Map<String, Integer> result = new HashMap<>();
        int cnt = likeService.getLikeCnt(postId, postType);
        result.put("likeCnt",cnt );
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }

    // 좋아요 했는지 체크하기 ( READ )
    @GetMapping("/check/{postId}/{postType}/{userId}")
    public ResponseEntity<BasicResponse> checkLike(@PathVariable Integer postId, @PathVariable Character postType, @PathVariable int userId) {
        Map<String, Boolean> result = new HashMap<>();
        boolean likePushed = likeService.likePushed(postId, postType, userId);
        result.put("likePushed",likePushed );
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, result);
    }

    // 좋아요 누르기 ( CREATE )
    @GetMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> hitLike(@PathVariable Integer postId, @PathVariable Character postType, @RequestHeader int userId) throws JsonProcessingException {
        likeService.hitLike(postId, postType, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 좋아요를 눌렀습니다.");
    }

    // 좋아요 취소 ( DELETE )
    @DeleteMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> cancleLike(@PathVariable Integer postId, @PathVariable Character postType, @RequestHeader int userId) {
        likeService.cancleLike(postId, postType, userId);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "좋아요를 취소했습니다.");
    }
}
