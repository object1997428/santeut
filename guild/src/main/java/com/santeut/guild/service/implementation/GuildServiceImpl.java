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

//        boolean existedGuild = guildRepository.existsByGuildName(request.getGuildName());
//        if (existedGuild) throw new DataNotFoundException(ResponseCode.EXISTS_GUILD_NAME);

        GuildEntity guildEntity = GuildEntity.createGuild(request, Integer.parseInt(userId));
        if (multipartFile != null){
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
        }
        guildEntity = guildRepository.save(guildEntity);

//        guildEntity = guildRepository.findByGuildName(request.getGuildName())
//                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

//        guildEntity = guildRepository.findLastRecord()
//                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        String regionName = regionUtil.getRegionName(request.getRegionId());
        RegionEntity regionEntity = new RegionEntity(request.getRegionId(), regionName);

        GuildUserEntity guildUserEntity = GuildUserEntity.createGuildUser(guildEntity.getUserId(), guildEntity.getGuildId());

        regionRepository.save(regionEntity);
        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public GetDetailGuildWithStatusResponse getDetailGuild(int guildId, int userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        GuildRequestEntity guildRequestEntity = guildRequestRepository.findByGuildIdAndUserId(guildId, userId)
                .orElse(null);

        char status = 'N';
        if (guildRequestEntity != null){
            if (guildRequestEntity.getStatus() == 'R') status = 'R';
            else if(guildRequestEntity.getStatus() == 'A') status = 'A';
        }
        return new GetDetailGuildWithStatusResponse(guildEntity, status);
    }

    @Override
    public void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

//        boolean existedGuildName = guildRepository.existsByGuildName(request.getGuildName());
//        if (existedGuildName) throw new DataNotFoundException(ResponseCode.EXISTS_GUILD_NAME);

        if (request != null) {
            guildEntity = GuildEntity.patchGuild(request, guildEntity);
        }

        if (multipartFile != null) {
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
            guildRepository.save(guildEntity);
        }

//        if (request != null && request.getRegionId() != null){
//        String regionName = regionUtil.getRegionName(request.getRegionId());
//        RegionEntity regionEntity = new RegionEntity(request.getRegionId(), regionName);
//        regionRepository.save(regionEntity);
//        }

//        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildId(guildId)
//                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));
//
//        guildUserEntity.setModifiedAt(LocalDateTime.now());
//        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public void deleteGuild(int guildId, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        guildEntity.setDeleted(true);
        guildEntity.setDeletedAt(LocalDateTime.now());

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildId(guildId)
                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));

        guildUserEntity.setDeleted(true);
        guildUserEntity.setDeletedAt(LocalDateTime.now());

        guildRepository.save(guildEntity);
        guildUserRepository.save(guildUserEntity);
    }

    @Override
    public GetGuildListResponse getGuildList() {

        List<GuildEntity> guildList = guildRepository.findByAllGuild();
        if(guildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        return new GetGuildListResponse(GetDetailGuildResponse.guildList(guildList));
    }

    @Override
    public GetMyGuildResponse myGuildList(String userId) {

        List<GuildEntity> myguildList = guildRepository.findByMyGuild(Integer.parseInt(userId));
        if (myguildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        return new GetMyGuildResponse(GetDetailGuildResponse.guildList(myguildList));
    }

    @Override
    public SearchGuildListResponse searchGuildList(String regionName, String gender) {

        RegionEntity regionEntity = regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.WRONG_REGION_NAME));


        String genderType;
        if (gender.equals("남성")) genderType = "M";
        else if(gender.equals("여성")) genderType = "F";
        else genderType = "A";

        List<GuildEntity> searchGuildList = guildRepository.searchGuild(regionEntity.getRegionId(), genderType);
        return new SearchGuildListResponse(GetDetailGuildResponse.guildList(searchGuildList));
    }

    @Override
    public ShareLinkResponse shareLink(int guildId) {

        String url = "https://www.google.com/search?q=%EA%B3%A0%EC%96%91%EC%9D%B4&oq=%EA%B3%A0%EC%96%91%EC%9D%B4&gs_lcrp=EgZjaHJvbWUyBggAEEUYOdIBCTM5MzFqMGoxNagCALACAA&sourceid=chrome&ie=UTF-8";
        return new ShareLinkResponse(url);
    }
}
