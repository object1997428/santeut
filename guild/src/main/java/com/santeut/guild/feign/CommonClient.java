package com.santeut.guild.feign;

import com.santeut.guild.feign.dto.AlarmRequestDto;
import com.santeut.guild.feign.dto.CommentListFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(name = "commonClient", url="${common-service.url}")
public interface CommonClient {

    // 좋아요 수 가져오기
    @GetMapping("/like/cnt/{postId}/{postType}")
//    Optional<ApiResponse<Map<String,Integer>>> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType);
    Optional<FeignResponseDto<Map<String,Integer>>> getLikeCnt(@PathVariable Integer postId, @PathVariable Character postType);

    // 알람 생성
    @PostMapping("/alarm/{referenceId}/{referenceType}")
    ResponseEntity<?> createAlarm(@PathVariable Integer referenceId,
                                  @PathVariable String referenceType,
                                  @RequestBody AlarmRequestDto requestDto);

    // 댓글 개수 가져오기
    @GetMapping("/comment/cnt/{postId}/{postType}")
    Optional<FeignResponseDto<Map<String,Integer>>> getCommentCnt(@PathVariable  Integer postId, @PathVariable Character postType);

    // comment controller 요청
    @GetMapping("/comment/{postId}/{postType}")
     Optional<FeignResponseDto<CommentListFeignDto>> getCommentList(@PathVariable  Integer postId, @PathVariable Character postType);

    @GetMapping("/like/check/{postId}/{postType}/{userId}")
    Optional<FeignResponseDto<Map<String,Boolean>>> likePushed(@PathVariable Integer postId, @PathVariable Character postType, @PathVariable Integer userId);

    // 이미지 S3에 저장 하고 DB에 저장
    @PostMapping(value="/image/save/{referenceId}/{referenceType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void saveImage(@PathVariable int referenceId, @PathVariable char referenceType ,@RequestPart List<MultipartFile> images);


    // 게시글 이미지 리스트 불러오기
    @GetMapping("/image/{referenceId}/{referenceType}")
    Optional<FeignResponseDto<List<String>>> getImages(@PathVariable  Integer referenceId, @PathVariable Character referenceType);
}
