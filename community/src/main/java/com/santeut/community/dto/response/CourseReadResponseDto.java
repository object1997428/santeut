package com.santeut.community.dto.response;

import com.santeut.community.feign.dto.CommentListFeignDto;
import com.santeut.community.feign.dto.FeignPartyLatLngResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseReadResponseDto {

    private int postId;

    private int userPartyId;

    // 게시글 작성자
    private int userId;

    private String postTitle;

    private String postContent;

    private char postType;

    private LocalDateTime createdAt;

    private List<FeignPartyLatLngResponseDto.Point> locationDataList; // 좌표 모음

    private String userNickname;

    private int likeCnt;

    private int commentCnt;

    private int hitCnt;

    private boolean isWriter;

    private boolean isLike;

    private List<CommentListFeignDto.Comment> commentList;

}
