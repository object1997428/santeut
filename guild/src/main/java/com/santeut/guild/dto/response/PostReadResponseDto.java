package com.santeut.guild.dto.response;

import com.santeut.guild.feign.dto.CommentFeignDto;
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

    private char postType = 'G';

    private int categoryId;

    private String categoryName;

    private String guildPostTitle;

    private String guildPostContent;

    private String userNickname;

    private LocalDateTime createdAt;

    private int likeCnt;

    private int commentCnt;

    private int hitCnt;

    private boolean isWriter;

    private boolean isLike;

    private List<CommentFeignDto> commentList;

    private List<String> images;
}
