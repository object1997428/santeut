package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildRequestEntity;
import com.santeut.guild.entity.GuildUserEntity;
import com.santeut.guild.repository.GuildUserRepository;
import lombok.*;

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
    char joinStatus;
    Boolean isPresident;
    Boolean isPrivate;

    public GetDetailGuildResponse(GuildEntity guildEntity, char joinStatus, Boolean isPresident, Boolean isPrivate){
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
        this.joinStatus = joinStatus;
        this.isPresident = isPresident;
        this.isPrivate = isPrivate;
    }

    public static List<GetDetailGuildResponse> guildList(List<GuildEntity> guildEntityList,
                                                         List<GuildRequestEntity> guildRequestList,
                                                         int userId){

        List<GetDetailGuildResponse> list = new ArrayList<>();
        for(int i=0; i<guildEntityList.size(); i++){

            GetDetailGuildResponse guildListResponse = new GetDetailGuildResponse();
            guildListResponse.guildId = guildEntityList.get(i).getGuildId();
            guildListResponse.guildName = guildEntityList.get(i).getGuildName();
            guildListResponse.guildProfile = guildEntityList.get(i).getGuildProfile();
            guildListResponse.guildInfo = guildEntityList.get(i).getGuildInfo();
            guildListResponse.guildMember = guildEntityList.get(i).getGuildMember();
            guildListResponse.regionId = guildEntityList.get(i).getRegionId();
            guildListResponse.guildGender = guildEntityList.get(i).getGuildGender();
            guildListResponse.guildMinAge = guildEntityList.get(i).getGuildMinAge();
            guildListResponse.guildMaxAge = guildEntityList.get(i).getGuildMaxAge();
            guildListResponse.createdAt = guildEntityList.get(i).getCreatedAt();
            if (guildRequestList.get(i) != null && guildRequestList.get(i).getStatus() == 'R') {
                guildListResponse.joinStatus = 'R';
            }
            else if (guildRequestList.get(i) != null && guildRequestList.get(i).getStatus() == 'A') {
                guildListResponse.joinStatus = 'A';
            }
            else {
                guildListResponse.joinStatus = 'N';
            }
            if (guildEntityList.get(i).getUserId() == userId) guildListResponse.isPresident = true;
            else guildListResponse.isPresident = false;

            list.add(guildListResponse);
        }
        return list;
    }
}
