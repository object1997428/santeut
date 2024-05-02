package com.santeut.community.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Builder
public class PostReadResponseDto {

    private int postId;

    private char postType;

    private String postTitle;

    private String postContent;

    private String nickName;

    private LocalDate createdAt;

    private int likeCnt;

    private int commentCnt;

    private int hitCnt;

    private List<CommentListResponseDto.Comment> commentList;

}
