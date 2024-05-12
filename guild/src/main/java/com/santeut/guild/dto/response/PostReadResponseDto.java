package com.santeut.guild.dto.response;

import com.santeut.guild.feign.dto.CommentFeignDto;
import com.santeut.guild.feign.dto.CommentListFeignDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PostReadResponseDto {

    private int guildPostId;

    private int guildId;

    @Builder.Default
    private char postType = 'G';

    private int categoryId;

    private String categoryName;

    private String guildPostTitle;

    private String guildPostContent;

    private String userNickname;

    private int userId;

    private LocalDateTime createdAt;

    private int likeCnt;

    private int commentCnt;

    private int hitCnt;

    private boolean isWriter;

    private boolean isLike;

    private List<CommentListFeignDto.Comment> commentList;

    private List<String> images;
}
