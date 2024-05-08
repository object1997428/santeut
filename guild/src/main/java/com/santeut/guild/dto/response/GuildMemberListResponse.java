package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class GuildMemberListResponse {

     List<GuildMemberInfo> memberList = new ArrayList<>();

     public static List<GuildMemberInfo> memberList(BasicResponse response){
          LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();
          List<GuildMemberInfo> list = new ArrayList<>();

          GuildMemberInfo guildMemberInfo = new GuildMemberInfo((Integer)data.get("userId"),
                  (String)data.get("userProfile"), (String)data.get("userNickname"));

          list.add(guildMemberInfo);
          return list;
     }

     @NoArgsConstructor
     @AllArgsConstructor
     @Data
     public static class GuildMemberInfo{
          int userId;
          String userProfile;
          String userNickname;
     }
}


