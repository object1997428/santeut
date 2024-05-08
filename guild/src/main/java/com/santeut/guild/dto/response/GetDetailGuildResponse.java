package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDetailGuildResponse {

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

    public GetDetailGuildResponse(GuildEntity guildEntity){
        this.guildId = guildEntity.getGuildId();
        this.guildName = guildEntity.getGuildName();
        this.guildProfile = guildEntity.getGuildProfile();
        this.guildInfo = guildEntity.getGuildInfo();
        this.guildMember = guildEntity.getGuildMember();
        this.regionId = guildEntity.getRegionId();
        this.guildGender = guildEntity.getGuildGender();
        this.guildMinAge = guildEntity.getGuildMinAge();
        this.guildMaxAge = guildEntity.getGuildMaxAge();
        this.createdAt = guildEntity.getCreatedAt();
    }

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
