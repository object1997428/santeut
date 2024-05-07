package com.santeut.guild.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListResponseDto {

    private int guildPostId;

    private int categoryId;

    private int guildId;

    private int userId;

    private String guildPostTitle;

    private String guildPostContent;

    private LocalDateTime createdAt;

    private Integer likeCnt;

    private Integer commentCnt;

    private Integer hitCnt;
}
