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
import com.santeut.guild.feign.CommonClient;
import com.santeut.guild.feign.UserFeign;
import com.santeut.guild.feign.dto.AlarmRequestDto;
import com.santeut.guild.repository.GuildRepository;
import com.santeut.guild.repository.GuildRequestRepository;
import com.santeut.guild.repository.GuildUserRepository;
import com.santeut.guild.service.GuildUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildUserServiceImpl implements GuildUserService {

    private final GuildRepository guildRepository;
    private final GuildUserRepository guildUserRepository;
    private final GuildRequestRepository guildRequestRepository;
    private final UserFeign userFeign;
    private final CommonClient commonClient;

    @Transactional
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
            else if(guildRequestEntity.getStatus() == 'D') guildRequestEntity.setStatus('R');
            else if(guildRequestEntity.getStatus() == 'C') guildRequestEntity.setStatus('R');
            guildRequestRepository.save(guildRequestEntity);
        }
        else {

        guildRequestEntity = GuildRequestEntity.requestGuild(guildId, Integer.parseInt(userId), 'R');
        guildRequestRepository.save(guildRequestEntity);
        }

        String referenceType = "GR";

        BasicResponse response = userFeign.userInfo(Integer.parseInt(userId));
        log.debug("Response: "+ response);
        if (response.getStatus() != 200) throw new DataNotFoundException(ResponseCode.FEIGN_ERROR);

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.getData();

        AlarmRequestDto alarmRequestDto = AlarmRequestDto.builder()
                .userId(guildEntity.getUserId())
                .referenceType(referenceType)
                .referenceId(guildId)
                .alarmTitle("동호회 가입 신청 알림")
                .alarmContent(data.get("userNickname")+ "님으로 부터 "+ guildEntity.getGuildName()+" 동호회 가입 신청이 왔습니다.")
                .build();

        log.debug("alarmRequestDto: {} ", alarmRequestDto.getUserId());

        commonClient.createAlarm(guildId, referenceType, alarmRequestDto);

    }

    @Override
    public ApplyGuildListResponse applyGuildList(int guildId, String nowUserId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != Integer.parseInt(nowUserId)) {
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }
        List<GuildRequestEntity> requestEntityList = guildRequestRepository.findByGuildId(guildId);

        List<BasicResponse> responseList = new ArrayList<>();
        for (GuildRequestEntity requestEntity : requestEntityList){

        int userId = requestEntity.getUserId();
        BasicResponse response = userFeign.userInfo(userId);
        log.debug("Response: "+ response);

        if (response.getStatus() != 200) throw new DataNotFoundException(ResponseCode.FEIGN_ERROR);
            responseList.add(response);
        }

        guildRepository.save(guildEntity);
        return new ApplyGuildListResponse(ApplyGuildListResponse.applyGuildList(responseList, requestEntityList));
    }

    @Transactional
    @Override
    public void approveApply(int guildId, int userId, String leaderUserId) {

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_REQUEST));
        if (guildRequestEntity.getStatus() != 'R') throw new DataNotFoundException(ResponseCode.NOT_REQUEST_USER);

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (guildEntity.getUserId() != Integer.parseInt(leaderUserId)){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);
        }

        guildRequestEntity.setModifiedAt(LocalDateTime.now());
        guildRequestEntity.setStatus('A');
        guildRequestRepository.save(guildRequestEntity);

        GuildUserEntity guildUserEntity = GuildUserEntity.createGuildUser(userId, guildId);
        guildEntity.setGuildMember(guildEntity.getGuildMember()+1);
        guildUserRepository.save(guildUserEntity);

        String referenceType = "GR";

        AlarmRequestDto alarmRequestDto = AlarmRequestDto.builder()
                .userId(userId)
                .referenceType(referenceType)
                .referenceId(guildId)
                .alarmTitle("동호회 가입 승인 알림")
                .alarmContent(guildEntity.getGuildName()+" 동호회 가입 신청이 승인되었습니다.")
                .build();

        log.debug("alarmRequestDto: {} ", alarmRequestDto.getUserId());

        commonClient.createAlarm(guildId, referenceType, alarmRequestDto);
    }

    @Override
    public void denyApply(int guildId, int userId, String leaderUserId) {

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_REQUEST));
        if (guildRequestEntity.getStatus() != 'R') throw new DataNotFoundException(ResponseCode.NOT_REQUEST_USER);

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
    public GuildMemberListResponse memberList(int guildId) {

        List<GuildUserEntity> guildUserEntityList = guildUserRepository.findByGuildUserList(guildId);
        if (guildUserEntityList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        List<BasicResponse> responseList = new ArrayList<>();
        for(GuildUserEntity guildUserEntity : guildUserEntityList){

            int userId = guildUserEntity.getUserId();
            BasicResponse response = userFeign.userInfo(userId);
            log.debug("Response: "+ response);
            if (response.getStatus() != 200) throw new DataNotFoundException(ResponseCode.FEIGN_ERROR);
            responseList.add(response);
        }
        return new GuildMemberListResponse(GuildMemberListResponse.memberList(responseList, guildUserEntityList));
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
        guildEntity.setGuildMember(guildEntity.getGuildMember()-1);
        guildRepository.save(guildEntity);
    }

    @Override
    public void delegateLeader(int guildId, int newLeaderId, int oldLeaderId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildIdAndUserId(guildId, newLeaderId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));
        
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

        guildEntity.setGuildMember(guildEntity.getGuildMember()-1);
        guildRepository.save(guildEntity);
    }
}