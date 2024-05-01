package com.santeut.common.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
public class CommentListResponseDto {
    private List<CommentInfo> commentList;
    private Integer guildId;

    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class CommentInfo {
        private String userNickname;
        private String commentContent;
        private LocalDateTime createdAt;
    }
}
