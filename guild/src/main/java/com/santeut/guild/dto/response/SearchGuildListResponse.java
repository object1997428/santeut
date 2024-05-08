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
public class SearchGuildListResponse {

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

    public static List<SearchGuildListResponse> searchGuildList(List<GuildEntity> guildEntityList){

        List<SearchGuildListResponse> list = new ArrayList<>();

        for (GuildEntity guildEntity : guildEntityList){

            SearchGuildListResponse searchGuildListResponse = new SearchGuildListResponse();
            searchGuildListResponse.guildId = guildEntity.getGuildId();
            searchGuildListResponse.guildName = guildEntity.getGuildName();
            searchGuildListResponse.guildProfile = guildEntity.getGuildProfile();
            searchGuildListResponse.guildInfo = guildEntity.getGuildInfo();
            searchGuildListResponse.guildMember = guildEntity.getGuildMember();
            searchGuildListResponse.regionId = guildEntity.getRegionId();
            searchGuildListResponse.guildGender = guildEntity.getGuildGender();
            searchGuildListResponse.guildMinAge = guildEntity.getGuildMinAge();
            searchGuildListResponse.guildMaxAge = guildEntity.getGuildMaxAge();
            searchGuildListResponse.createdAt = guildEntity.getCreatedAt();

            list.add(searchGuildListResponse);
        }
        return list;
    }
}
