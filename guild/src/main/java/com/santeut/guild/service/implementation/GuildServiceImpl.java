package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.ResponseCode;
import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.GetDetailGuildResponse;
import com.santeut.guild.dto.response.GetGuildListResponse;
import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.Image;
import com.santeut.guild.repository.GuildRepository;
import com.santeut.guild.service.GuildService;
import com.santeut.guild.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class GuildServiceImpl implements GuildService {

    private final ImageUtil imageUtil;
    private final GuildRepository guildRepository;

    @Override
    public void createGuild(CreateGuildRequest request, String userId, MultipartFile multipartFile) {

        boolean existedGuild = guildRepository.existsByGuildName(request.getGuildName());
        if (existedGuild) throw new DataNotFoundException(ResponseCode.EXISTS_GUILD_NAME);

        GuildEntity guildEntity = GuildEntity.createGuild(request, Integer.parseInt(userId));
        if (multipartFile != null){
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
        }

        guildRepository.save(guildEntity);
    }

    @Override
    public GetDetailGuildResponse getDetailGuild(int guildId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        return new GetDetailGuildResponse(guildEntity);
    }

    @Override
    public void patchGuildInfo(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        GuildEntity patchGuildEntity = GuildEntity.patchGuild(request, guildEntity);
        if (multipartFile != null){
            String imageUrl = imageUtil.saveImage(multipartFile);
            guildEntity.setGuildProfile(imageUrl);
        }

        guildRepository.save(patchGuildEntity);
    }

    @Override
    public void deleteGuild(int guildId, String userId) {

        GuildEntity guildEntity = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD));

        if (Integer.parseInt(userId) != guildEntity.getUserId()) throw new DataNotFoundException(ResponseCode.NOT_MATCH_GUILD_LEADER);

        guildEntity.setDeleted(true);
        guildRepository.save(guildEntity);
    }

    @Override
    public List<GetGuildListResponse> getGuildList() {

        List<GuildEntity> guildList = guildRepository.findByAllGuild();
        if(guildList == null) throw new DataNotFoundException(ResponseCode.NOT_EXISTS_GUILD);

        return GetGuildListResponse.guildList(guildList);
    }

}
