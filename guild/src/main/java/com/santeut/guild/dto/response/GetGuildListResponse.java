package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetGuildListResponse {

    List<GetDetailGuildResponse> guildList = new ArrayList<>();

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class GuildInfo{
//        int guildId;
//        String guildName;
//        String guildProfile;
//        String guildInfo;
//        int guildMember;
//        int regionId;
//        char guildGender;
//        int guildMinAge;
//        int guildMaxAge;
//        LocalDateTime createdAt;
//    }

    public static List<GetDetailGuildResponse> guildList(List<GuildEntity> guildEntityList){

        List<GetDetailGuildResponse> list = new ArrayList<>();

        for (GuildEntity guildEntity : guildEntityList){

            GetDetailGuildResponse guildListResponse = new GetDetailGuildResponse();
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
