package com.santeut.guild.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyMemberInfo {
    private int userId;
    private String userNickname;
    private String userProfile;
}
