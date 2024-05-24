package com.santeut.guild.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListResponseDto {

    private List<Post> postList;

    @Data
    @AllArgsConstructor
    @Builder
    @Setter
    public static class Post {

        private int guildPostId;

        private int categoryId;

        private int guildId;

        private int userId;

        private String userNickName;

        private String guildPostTitle;

        private String guildPostContent;

        private LocalDateTime createdAt;

        private Integer likeCnt;

        private Integer commentCnt;

        private Integer hitCnt;
    }

}
