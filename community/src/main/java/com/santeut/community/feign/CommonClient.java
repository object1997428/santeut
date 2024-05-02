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
    @GetMapping("/like/{postId}/{postType}")
    Optional<Map<String, Integer>> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType);
    @GetMapping("/like/cnt/{postId}/{postType}")
    Optional<Map<String, Integer>> getCommentCnt(Integer postId, Character postType);

    // comment controller 요청
    @GetMapping("/comment/{postId}/{postType}")
     Optional<CommentListResponseDto> getCommentList(Integer postId, Character postType);

}
