package com.santeut.guild.feign;

import com.santeut.guild.feign.dto.CommentFeignDto;
import com.santeut.guild.feign.dto.CommentListFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(name = "commonClient", url="${common-service.url}")
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
     Optional<FeignResponseDto<CommentListFeignDto>> getCommentList(@PathVariable  Integer postId, @PathVariable Character postType);

    @GetMapping("/like/check/{postId}/{postType}/{userId}")
    Optional<FeignResponseDto<Map<String,Boolean>>> likePushed(@PathVariable Integer postId, @PathVariable Character postType, @PathVariable Integer userId);

    // 이미지 s3에 저장
    @PostMapping("/image/upload/{referenceId}/{referenceType}")
    void saveImageUrl(@PathVariable  Integer referenceId, @PathVariable Character referenceType, @RequestBody Map<String, String> imageUrl);

    // 게시글 이미지 리스트 불러오기
    @GetMapping("/image/{referenceId}/{referenceType}")
    Optional<FeignResponseDto<List<String>>> getImages(@PathVariable  Integer referenceId, @PathVariable Character referenceType);
}
