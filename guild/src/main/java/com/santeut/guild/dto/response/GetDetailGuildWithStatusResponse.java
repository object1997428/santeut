package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailGuildWithStatusResponse {
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
    char joinStatus;
    Boolean isPresident;

    public GetDetailGuildWithStatusResponse(GuildEntity guildEntity, char status, Boolean isPresident){
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
        this.joinStatus = status;
        this.isPresident = isPresident;
    }
}
