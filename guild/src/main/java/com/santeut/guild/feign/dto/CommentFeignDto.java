package com.santeut.guild.feign.dto;

import java.time.LocalDateTime;

public class CommentFeignDto {
    private int commentId;

    private int userId;

    private String userNickname;

    private String commentContent;

    private LocalDateTime createdAt;
}
