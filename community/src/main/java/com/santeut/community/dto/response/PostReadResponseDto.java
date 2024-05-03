package com.santeut.community.dto.response;

import com.santeut.community.feign.dto.CommentListFeignDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class PostReadResponseDto {

    private int postId;

    private char postType;

    private String postTitle;

    private String postContent;

    private String userNickname;

    private LocalDateTime createdAt;

    private int likeCnt;

    private int commentCnt;

    private int hitCnt;

    private List<CommentListFeignDto.Comment> commentList;

}
