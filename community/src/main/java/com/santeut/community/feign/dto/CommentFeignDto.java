package com.santeut.community.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentFeignDto {
    private String userNickname;
    private String commentContent;
    private LocalDateTime createdAt;
}
