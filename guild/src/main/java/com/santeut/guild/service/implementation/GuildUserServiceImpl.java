package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.common.response.ResponseCode;
import com.santeut.guild.dto.response.ApplyGuildListResponse;
import com.santeut.guild.entity.GuildRequestEntity;
import com.santeut.guild.feign.UserFeign;
import com.santeut.guild.repository.GuildRequestRepository;
import com.santeut.guild.repository.GuildUserRepository;
import com.santeut.guild.service.GuildUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildUserServiceImpl implements GuildUserService {

    private final GuildUserRepository guildUserRepository;
    private final GuildRequestRepository guildRequestRepository;
    private final UserFeign userFeign;
    @Override
    public void applyGuild(int guildId, String userId) {

        GuildRequestEntity guildRequestEntity =
                guildRequestRepository.findByGuildIdAndUserId(guildId, Integer.parseInt(userId));

        if (guildRequestEntity != null){
            if(guildRequestEntity.getStatus() == 'R') throw new DataNotFoundException(ResponseCode.ALREADY_REQUEST);
            else if(guildRequestEntity.getStatus() == 'A') throw new DataNotFoundException(ResponseCode.ALREADY_APPROVE);
        }

        guildRequestEntity = GuildRequestEntity.requestGuild(guildId, Integer.parseInt(userId));
        guildRequestRepository.save(guildRequestEntity);
    }

    @Override
    public List<ApplyGuildListResponse> applyGuildList(int guildId) {

        List<GuildRequestEntity> requestEntityList = guildRequestRepository.findByGuildId(guildId);
        List<ApplyGuildListResponse> applyGuildListResponseList = new ArrayList<>();

        for (GuildRequestEntity requestEntity : requestEntityList){

        int userId = requestEntity.getUserId();
        BasicResponse response = userFeign.userInfo(userId);

        log.debug("Data: "+ response.getData());

        applyGuildListResponseList.add(new ApplyGuildListResponse(response, requestEntity));

        }

        return applyGuildListResponseList;
    }
}