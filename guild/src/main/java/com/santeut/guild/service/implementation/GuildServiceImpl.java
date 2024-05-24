package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.ResponseCode;
import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.*;
import com.santeut.guild.entity.*;
import com.santeut.guild.repository.GuildRepository;
import com.santeut.guild.repository.GuildRequestRepository;
import com.santeut.guild.repository.GuildUserRepository;
import com.santeut.guild.repository.RegionRepository;
import com.santeut.guild.service.GuildService;
import com.santeut.guild.util.ImageUtil;
import com.santeut.guild.util.RegionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class GuildServiceImpl implements GuildService {

    private final ImageUtil imageUtil;
    private final GuildRepository guildRepository;
    private final RegionRepository regionRepository;
    private final RegionUtil regionUtil;
    private final GuildUserRepository guildUserRepository;
    private final GuildRequestRepository guildRequestRepository;

    @Override
    public void createGuild(CreateGuildRequest request, String userId, MultipartFile multipartFile) {

        GuildEntity guildEntity = GuildEntity.createGuild(request, Integer.parseInt(userId));
        if (multipartFile != null){
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
        }
        guildEntity = guildRepository.save(guildEntity);

        String regionName = regionUtil.getRegionName(request.getRegionId());
        RegionEntity regionEntity = new RegionEntity(request.getRegionId(), regionName);

        GuildUserEntity guildUserEntity = GuildUserEntity.createGuildUser(guildEntity.getUserId(), guildEntity.getGuildId());

        GuildRequestEntity guildRequestEntity =
                GuildRequestEntity.requestGuild(guildEntity.getGuildId(), Integer.parseInt(userId), 'A');
        regionRepository.save(regionEntity);
        guildUserRepository.save(guildUserEntity);
        guildRequestRepository.save(guildRequestEntity);
    }

    @Override
    public GetDetailGuildResponse getDetailGuild(int guildId, int userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElse(null);

        char status = 'N';
        if (guildRequestEntity != null){
            if (guildRequestEntity.getStatus() == 'R') status = 'R';
            else if(guildRequestEntity.getStatus() == 'A') status = 'A';
        }

        boolean isPrivate = guildEntity.getGuildIsPrivate();
        boolean isPresident = false;
        if (guildEntity.getUserId() == userId) isPresident = true;

        return new GetDetailGuildResponse(guildEntity, status, isPresident, isPrivate);
    }

    @Override
    public void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        if (request != null) {
            guildEntity = GuildEntity.patchGuild(request, guildEntity);
            guildRepository.save(guildEntity);
        }

        if (multipartFile != null) {
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
            guildRepository.save(guildEntity);
        }
    }

    @Override
    @Transactional
    public void deleteGuild(int guildId, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        guildEntity.setDeleted(true);
        guildEntity.setDeletedAt(LocalDateTime.now());
        guildRepository.save(guildEntity);

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildId(guildId)
                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));

        guildUserEntity.setDeleted(true);
        guildUserEntity.setDeletedAt(LocalDateTime.now());
        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public GetGuildListResponse getGuildList(int userId) {

        List<GuildEntity> guildList = guildRepository.findByAllGuild();
        if(guildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        List<GuildRequestEntity> guildRequestList = new ArrayList<>();
        for (GuildEntity guildEntity : guildList){

            GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildEntity.getGuildId(), userId)
                    .orElse(null);
            if(guildRequestEntity != null) guildRequestList.add(guildRequestEntity);
            else guildRequestList.add(null);
        }

        return new GetGuildListResponse(GetDetailGuildResponse.guildList(guildList, guildRequestList, userId));
    }

    @Override
    public GetMyGuildResponse myGuildList(int userId) {

        List<GuildEntity> myguildList = guildRepository.findByMyGuild(userId);
        if (myguildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        List<GuildRequestEntity> guildRequestList = new ArrayList<>();
        for (GuildEntity guildEntity : myguildList){

            GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildEntity.getGuildId(), userId)
                    .orElse(null);
            if(guildRequestEntity != null) guildRequestList.add(guildRequestEntity);
            else guildRequestList.add(null);
        }

        return new GetMyGuildResponse(GetDetailGuildResponse.guildList(myguildList, guildRequestList, userId));
    }

    @Override
    public SearchGuildListResponse searchGuildList(String regionName, String gender, int userId) {

        RegionEntity regionEntity = regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.WRONG_REGION_NAME));


        String genderType;
        if (gender.equals("남성")) genderType = "M";
        else if(gender.equals("여성")) genderType = "F";
        else genderType = "A";

        List<GuildEntity> searchGuildList = guildRepository.searchGuild(regionEntity.getRegionId(), genderType);

        List<GuildRequestEntity> guildRequestList = new ArrayList<>();
        for (GuildEntity guildEntity : searchGuildList){

            GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildEntity.getGuildId(), userId)
                    .orElse(null);
            if(guildRequestEntity != null) guildRequestList.add(guildRequestEntity);
            else guildRequestList.add(null);

        }

        return new SearchGuildListResponse(GetDetailGuildResponse.guildList(searchGuildList, guildRequestList, userId));
    }

    @Override
    public SearchGuildNameListResponse searchGuildName(String guildName, int userId) {

        List<GuildEntity> searchGuildList = guildRepository.searchGuildName(guildName);
        if (searchGuildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        List<GuildRequestEntity> guildRequestList = new ArrayList<>();
        for (GuildEntity guildEntity : searchGuildList){

            GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildEntity.getGuildId(), userId)
                    .orElse(null);
            if(guildRequestEntity != null) guildRequestList.add(guildRequestEntity);
            else guildRequestList.add(null);

        }

        return new SearchGuildNameListResponse(GetDetailGuildResponse.guildList(searchGuildList, guildRequestList, userId));
    }
}
