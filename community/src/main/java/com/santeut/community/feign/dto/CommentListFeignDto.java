package com.santeut.community.feign.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListFeignDto {
    private List<Comment> commentList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment {
        private int commentId;

        private int userId;

        private String userNickname;

        private String commentContent;

        private LocalDateTime createdAt;
    }
}
