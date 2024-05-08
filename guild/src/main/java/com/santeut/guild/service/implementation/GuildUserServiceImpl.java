package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.BasicResponse;
import com.santeut.guild.common.response.ResponseCode;
import com.santeut.guild.dto.response.ApplyGuildListResponse;
import com.santeut.guild.dto.response.GuildMemberListResponse;
import com.santeut.guild.dto.response.UserInfoResponse;
import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildRequestEntity;
import com.santeut.guild.entity.GuildUserEntity;
import com.santeut.guild.feign.UserFeign;
import com.santeut.guild.repository.GuildRepository;
import com.santeut.guild.repository.GuildRequestRepository;
import com.santeut.guild.repository.GuildUserRepository;
import com.santeut.guild.service.GuildUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildUserServiceImpl implements GuildUserService {

    private final GuildRepository guildRepository;
    private final GuildUserRepository guildUserRepository;
    private final GuildRequestRepository guildRequestRepository;
    private final UserFeign userFeign;
    @Override
    public void applyGuild(int guildId, String userId) {

        GuildRequestEntity guildRequestEntity =
                guildRequestRepository.findByGuildIdAndUserId(guildId, Integer.parseInt(userId))
                        .orElse(null);

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));
        if(guildEntity.getUserId() == Integer.parseInt(userId)) throw new DataNotFoundException(ResponseCode.ALREADY_APPROVE);

        if (guildRequestEntity != null){
            if(guildRequestEntity.getStatus() == 'R') throw new DataNotFoundException(ResponseCode.ALREADY_REQUEST);
            else if(guildRequestEntity.getStatus() == 'A') throw new DataNotFoundException(ResponseCode.ALREADY_APPROVE);
        }

        // 동호회장을 위임하고 가입 요청을 보내는 경우 방지
        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildIdAndUserId(guildId, Integer.parseInt(userId))
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));

        guildRequestEntity = GuildRequestEntity.requestGuild(guildId, Integer.parseInt(userId));
        guildRequestRepository.save(guildRequestEntity);
    }

    @Override
    public List<ApplyGuildListResponse> applyGuildList(int guildId, String nowUserId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != Integer.parseInt(nowUserId)) {
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }
        List<GuildRequestEntity> requestEntityList = guildRequestRepository.findByGuildId(guildId);
        List<ApplyGuildListResponse> applyGuildListResponseList = new ArrayList<>();

        for (GuildRequestEntity requestEntity : requestEntityList){

        int userId = requestEntity.getUserId();
        BasicResponse response = userFeign.userInfo(userId);
        log.debug("Response: "+ response);

        if (response.getStatus() != 200) throw new DataNotFoundException(ResponseCode.FEIGN_ERROR);
        applyGuildListResponseList.add(new ApplyGuildListResponse(response, requestEntity));
        }

        return applyGuildListResponseList;
    }

    @Override
    public void approveApply(int guildId, int userId, String leaderUserId) {

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_REQUEST));

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != Integer.parseInt(leaderUserId)){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }

        guildRequestEntity.setModifiedAt(LocalDateTime.now());
        guildRequestEntity.setStatus('A');
        guildRequestRepository.save(guildRequestEntity);

        GuildUserEntity guildUserEntity = GuildUserEntity.createGuildUser(guildId, userId);
        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public void denyApply(int guildId, int userId, String leaderUserId) {

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_REQUEST));

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != Integer.parseInt(leaderUserId)){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }

        guildRequestEntity.setModifiedAt(LocalDateTime.now());
        guildRequestEntity.setStatus('D');
        guildRequestRepository.save(guildRequestEntity);

    }

    @Override
    public List<GuildMemberListResponse> memberList(int guildId) {

        List<GuildUserEntity> guildUserEntityList = guildUserRepository.findByGuildUserList(guildId);
        if (guildUserEntityList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        List<GuildMemberListResponse> guildMemberListResponseList = new ArrayList<>();

        for(GuildUserEntity guildUserEntity : guildUserEntityList){

            int userId = guildUserEntity.getUserId();
            BasicResponse response = userFeign.userInfo(userId);
            log.debug("Response: "+ response);

            if (response.getStatus() != 200) throw new DataNotFoundException(ResponseCode.FEIGN_ERROR);
            guildMemberListResponseList.add(new GuildMemberListResponse(response));
        }
        return guildMemberListResponseList;
    }

    @Override
    public void kickMember(int guildId, int userId, String leaderUserId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));
        if (guildEntity.getUserId() != Integer.parseInt(leaderUserId)) {
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_REQUEST));

        guildRequestEntity.setStatus('C');
        guildRequestEntity.setModifiedAt(LocalDateTime.now());
        guildRequestRepository.save(guildRequestEntity);

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildIdAndUserId(guildId, userId)
                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));
        guildUserEntity.setDeleted(true);
        guildUserEntity.setModifiedAt(LocalDateTime.now());
        guildUserEntity.setDeletedAt(LocalDateTime.now());
        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public void delegateLeader(int guildId, int newLeaderId, int oldLeaderId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != oldLeaderId) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        guildEntity.setUserId(newLeaderId);
        guildEntity.setModifiedAt(LocalDateTime.now());
        guildRepository.save(guildEntity);
    }

    @Override
    public void quitGuild(int guildId, int userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() == userId) throw new DataNotFoundException(ResponseCode.MUST_NOT_QUIT_GUILD_LEADER);

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElse(null);

        if (guildRequestEntity != null){
        guildRequestEntity.setModifiedAt(LocalDateTime.now());
        guildRequestEntity.setStatus('C');
        guildRequestRepository.save(guildRequestEntity);
        }

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));
        guildUserEntity.setDeleted(true);
        guildUserEntity.setModifiedAt(LocalDateTime.now());
        guildUserEntity.setDeletedAt(LocalDateTime.now());
        guildUserRepository.save(guildUserEntity);
    }
}