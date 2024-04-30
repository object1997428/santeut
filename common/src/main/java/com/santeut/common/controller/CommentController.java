package com.santeut.common.controller;

import com.santeut.common.common.response.BasicResponse;
import com.santeut.common.common.util.ResponseUtil;
import com.santeut.common.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 쓰기 (CREATE)
    @PostMapping("/{postId}/{postType}")
    public ResponseEntity<BasicResponse> createComment(@RequestBody String commentContent, @PathVariable Integer postId, @PathVariable Character postType) {
        commentService.createComment(postId, postType, commentContent);
        return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "댓글 작성 성공적으로 수행됨.");
    }
}
