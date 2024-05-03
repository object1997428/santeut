package com.santeut.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentListResponseDto {
    private List<Comment> commentList;
    @Data
    @AllArgsConstructor
    @Builder
    @Setter
    public static class Comment {
        private int commentId;

        private int userId;

        private String userNickname;

        private String commentContent;

        private LocalDateTime createdAt;
    }
}
