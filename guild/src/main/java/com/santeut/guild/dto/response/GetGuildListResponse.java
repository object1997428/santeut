package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetGuildListResponse {

    int guildId;
    String guildName;
    String guildProfile;
    String guildInfo;
    int guildMember;
    int regionId;
    int guildGender;
    int guildMinAge;
    int guildMaxAge;
    LocalDateTime createdAt;

    public static List<GetGuildListResponse> guildList(List<GuildEntity> guildEntityList){

        List<GetGuildListResponse> list = new ArrayList<>();

        for (GuildEntity guildEntity : guildEntityList){

            GetGuildListResponse guildListResponse = new GetGuildListResponse();
            guildListResponse.guildId = guildEntity.getGuildId();
            guildListResponse.guildName = guildEntity.getGuildName();
            guildListResponse.guildProfile = guildEntity.getGuildProfile();
            guildListResponse.guildInfo = guildEntity.getGuildInfo();
            guildListResponse.guildMember = guildEntity.getGuildMember();
            guildListResponse.regionId = guildEntity.getRegionId();
            guildListResponse.guildGender = guildEntity.getGuildGender();
            guildListResponse.guildMinAge = guildEntity.getGuildMinAge();
            guildListResponse.guildMaxAge = guildEntity.getGuildMaxAge();
            guildListResponse.createdAt = guildEntity.getCreatedAt();

            list.add(guildListResponse);
        }
        return list;
    }
}
