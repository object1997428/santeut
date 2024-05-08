package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;

@Getter
@AllArgsConstructor
public class GuildMemberListResponse {

     int userId;
     String userProfile;
     String userNickname;

     public GuildMemberListResponse(BasicResponse response){
          LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

          this.userId = (Integer)data.get("userId");
          this.userProfile = (String)data.get("userProfile");
          this.userNickname = (String)data.get("userNickname");
     }
}
