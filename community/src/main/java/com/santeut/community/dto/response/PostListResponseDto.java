package com.santeut.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostListResponseDto {
    private List<PostInfo> postList;
    @Data
    @AllArgsConstructor
    @Builder
    @Setter
    public static class PostInfo {

        private Integer postId;

        private Character postType;

        private String postTitle;

        private String postContent;

        private String userNickname; // 작성자 닉네임

        private LocalDateTime createdAt;

        private Integer likeCnt;

        private Integer commentCnt;

        private Integer hitCnt;
    }
}
