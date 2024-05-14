package com.santeut.guild.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankUserInfo {
    private int order;
    private String score;
    private int userId;
    private String userNickname;
    private String userProfile;
}
