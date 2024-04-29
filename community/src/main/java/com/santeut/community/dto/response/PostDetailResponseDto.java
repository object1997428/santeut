package com.santeut.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PostDetailResponseDto {

    private Integer postId;

    private Character postType;

    private String postTitle;

    private String postContent;

    private String imageUrl;

    private String userNickname; // 작성자 닉네임

    private LocalDateTime createdAt;

    private int hitCnt;

    private Integer likeCnt;

    private Integer commentCnt;
}
