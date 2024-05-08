package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.entity.GuildRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyGuildListResponse {

    List<ApplyGuildListInfo> applyGuildList = new ArrayList<>();

    public static List<ApplyGuildListInfo> applyGuildList(List<BasicResponse> responseList,
                                                          List<GuildRequestEntity> requestEntityList){

        List<ApplyGuildListInfo> list = new ArrayList<>();
        int responseListLength = responseList.size();

        for (int i=0; i<responseListLength; i++){

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) responseList.get(i).getData();
            list.add(new ApplyGuildListInfo(requestEntityList.get(i).getGuildRequestId(),
                    requestEntityList.get(i).getCreatedAt(), (Integer)data.get("userId"),
                    (String)data.get("userNickname"), (String)data.get("userProfile")));
        }

        return list;
    }

    @Data
    @AllArgsConstructor
    public static class ApplyGuildListInfo{
        int guildRequestId;
        LocalDateTime createdAt;
        int userId;
        String userNickname;
        String userProfile;
    }
}
