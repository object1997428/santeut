package com.santeut.guild.service;

import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.*;
import com.santeut.guild.entity.GuildEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GuildService {

    void createGuild(CreateGuildRequest request, String userId, MultipartFile multipartFile);
    GetDetailGuildWithStatusResponse getDetailGuild(int guildId, int userId);
    void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId);
    void deleteGuild(int guildId, String userId);
    GetGuildListResponse getGuildList();
    GetMyGuildResponse myGuildList(String userId);
    SearchGuildListResponse searchGuildList(String regionName, String gender);
    ShareLinkResponse shareLink(int guildId);
}
