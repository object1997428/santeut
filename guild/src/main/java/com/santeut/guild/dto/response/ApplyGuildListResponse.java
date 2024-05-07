package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.entity.GuildRequestEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplyGuildListResponse {

    int guildRequestId;
    String userName;
    String userProfile;
    LocalDateTime createdAt;

    public ApplyGuildListResponse(BasicResponse response, GuildRequestEntity entity){

        this.guildRequestId = entity.getGuildRequestId();
        this.createdAt = entity.getCreatedAt();
        this.userName = response.getData().toString();
        this.userProfile = response.getData().toString();

    }
}
