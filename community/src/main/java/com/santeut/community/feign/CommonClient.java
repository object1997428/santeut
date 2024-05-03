package com.santeut.community.feign;

import com.santeut.community.common.config.FeignConfiguration;
import com.santeut.community.dto.response.CommentListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Optional;

@FeignClient(name = "commonClient", url="${common-service.url}", configuration = FeignConfiguration.class)
public interface CommonClient {

    // 좋아요 수 가져오기
    @GetMapping("/like/cnt/{postId}/{postType}")
//    Optional<ApiResponse<Map<String,Integer>>> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType);
    Optional<FeignResponseDto<Map<String,Integer>>> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType);
    // 댓글 개수 가져오기
    @GetMapping("/comment/cnt/{postId}/{postType}")
    Optional<FeignResponseDto<Map<String,Integer>>> getCommentCnt(@PathVariable  Integer postId, @PathVariable Character postType);

    // comment controller 요청
    @GetMapping("/comment/{postId}/{postType}")
     Optional<CommentListResponseDto> getCommentList(@PathVariable  Integer postId, @PathVariable Character postType);

}
