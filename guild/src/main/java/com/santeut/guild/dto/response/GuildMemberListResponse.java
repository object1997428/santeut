package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class GuildMemberListResponse {

     List<GuildMemberInfo> memberList = new ArrayList<>();

     public static List<GuildMemberInfo> memberList(List<BasicResponse> responseList, List<GuildUserEntity> guildUserEntityList){

          List<GuildMemberInfo> list = new ArrayList<>();
          for (int i=0; i<responseList.size(); i++){

          LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) responseList.get(i).getData();

          GuildMemberInfo guildMemberInfo = new GuildMemberInfo((Integer)data.get("userId"),
                  (String)data.get("userProfile"), (String)data.get("userNickname"),
                 guildUserEntityList.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) );
          list.add(guildMemberInfo);
          }
          return list;
     }

     @NoArgsConstructor
     @AllArgsConstructor
     @Data
     public static class GuildMemberInfo{
          int userId;
          String userProfile;
          String userNickname;
          String joinDate;
     }
}


