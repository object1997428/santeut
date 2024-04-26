package com.santeut.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private List<PostInfo> postList;
    @Data
    @AllArgsConstructor
    @Builder
    public static class PostInfo {

        private Integer postId;

        private int userId;

        private String postTitle;

        private String postContent;

        private LocalDateTime createdAt;

        private Character postType;

        private Integer likeCnt;

        private Integer commentCnt;

        private Integer hit;
    }
}
