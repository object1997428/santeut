package com.santeut.guild.service;

import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.*;
import com.santeut.guild.entity.GuildEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GuildService {

    void createGuild(CreateGuildRequest request, String userId, MultipartFile multipartFile);
    GetDetailGuildResponse getDetailGuild(int guildId, int userId);
    void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId);
    void deleteGuild(int guildId, String userId);
    GetGuildListResponse getGuildList(int userId);
    GetMyGuildResponse myGuildList(int userId);
    SearchGuildListResponse searchGuildList(String regionName, String gender, int userId);
    SearchGuildNameListResponse searchGuildName(String guildName, int userId);
    ShareLinkResponse shareLink(int guildId);
}
