package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetDetailGuildResponse {

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
}
