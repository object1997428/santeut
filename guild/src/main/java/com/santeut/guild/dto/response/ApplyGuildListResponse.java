package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.entity.GuildRequestEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Getter
public class ApplyGuildListResponse {

    int guildRequestId;
    int userId;
    String userNickName;
    String userProfile;
    LocalDateTime createdAt;

    public ApplyGuildListResponse(BasicResponse response, GuildRequestEntity entity){
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        this.guildRequestId = entity.getGuildRequestId();
        this.createdAt = entity.getCreatedAt();
        this.userId = (Integer)data.get("userId");
        this.userNickName = (String)data.get("userNickname");
        this.userProfile = (String)data.get("userProfile");

    }
}
