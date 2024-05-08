package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetMyGuildResponse {

    int guildId;
    String guildName;
    String guildProfile;
    String guildInfo;
    int guildMember;
    int regionId;
    char guildGender;
    int guildMinAge;
    int guildMaxAge;
    LocalDateTime createdAt;

    public static List<GetMyGuildResponse> guildList(List<GuildEntity> guildEntityList){

        List<GetMyGuildResponse> list = new ArrayList<>();

        for (GuildEntity guildEntity : guildEntityList){

            GetMyGuildResponse myGuildListResponse = new GetMyGuildResponse();
            myGuildListResponse.guildId = guildEntity.getGuildId();
            myGuildListResponse.guildName = guildEntity.getGuildName();
            myGuildListResponse.guildProfile = guildEntity.getGuildProfile();
            myGuildListResponse.guildInfo = guildEntity.getGuildInfo();
            myGuildListResponse.guildMember = guildEntity.getGuildMember();
            myGuildListResponse.regionId = guildEntity.getRegionId();
            myGuildListResponse.guildGender = guildEntity.getGuildGender();
            myGuildListResponse.guildMinAge = guildEntity.getGuildMinAge();
            myGuildListResponse.guildMaxAge = guildEntity.getGuildMaxAge();
            myGuildListResponse.createdAt = guildEntity.getCreatedAt();

            list.add(myGuildListResponse);
        }
        return list;
    }
}
