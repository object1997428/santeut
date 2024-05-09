package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.ResponseCode;
import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.GetDetailGuildResponse;
import com.santeut.guild.dto.response.GetGuildListResponse;
import com.santeut.guild.dto.response.GetMyGuildResponse;
import com.santeut.guild.dto.response.SearchGuildListResponse;
import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildUserEntity;
import com.santeut.guild.entity.Image;
import com.santeut.guild.entity.RegionEntity;
import com.santeut.guild.repository.GuildRepository;
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
    public GetDetailGuildResponse getDetailGuild(int guildId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        return new GetDetailGuildResponse(guildEntity);
    }

    @Override
    public void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

//        boolean existedGuildName = guildRepository.existsByGuildName(request.getGuildName());
//        if (existedGuildName) throw new DataNotFoundException(ResponseCode.EXISTS_GUILD_NAME);

        GuildEntity patchGuildEntity = GuildEntity.patchGuild(request, guildEntity);

        if (multipartFile != null) {
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
            guildRepository.save(patchGuildEntity);
        }

        if (request.getRegionId() != null){
        String regionName = regionUtil.getRegionName(request.getRegionId());
        RegionEntity regionEntity = new RegionEntity(request.getRegionId(), regionName);
        regionRepository.save(regionEntity);
        }

        GuildUserEntity guildUserEntity = guildUserRepository.findByGuildId(guildId)
                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD_USER));

        guildUserEntity.setModifiedAt(LocalDateTime.now());
        guildUserRepository.save(guildUserEntity);
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

        List<GuildEntity> searchGuildList;

        if(!regionName.equals("전체") && gender.equals("전체")) {
            log.debug("지역 검색");
            searchGuildList = guildRepository.searchRegionName(regionEntity.getRegionId());
        }
        else if(regionName.equals("전체") && !gender.equals("전체")) {
            log.debug("성별 검색");
            searchGuildList = guildRepository.searchGender(gender);
        }
        else if(!regionName.equals("전체") && !gender.equals("전체")) {
            log.debug("지역 검색 & 성별 검색");
            searchGuildList = guildRepository.searchRegionNameAndGender(regionEntity.getRegionId(), gender);
        }
        else {
            log.debug("전체 검색");
            searchGuildList = guildRepository.findByAllGuild();
        }
        return new SearchGuildListResponse(GetDetailGuildResponse.guildList(searchGuildList));
    }
}
