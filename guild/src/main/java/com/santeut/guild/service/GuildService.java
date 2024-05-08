package com.santeut.guild.service;

import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import com.santeut.guild.dto.response.GetDetailGuildResponse;
import com.santeut.guild.dto.response.GetGuildListResponse;
import com.santeut.guild.dto.response.GetMyGuildResponse;
import com.santeut.guild.dto.response.SearchGuildListResponse;
import com.santeut.guild.entity.GuildEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GuildService {

    void createGuild(CreateGuildRequest request, String userId, MultipartFile multipartFile);
    GetDetailGuildResponse getDetailGuild(int guildId);
    void patchGuild(int guildId, PatchGuildInfoRequest request, MultipartFile multipartFile, String userId);
    void deleteGuild(int guildId, String userId);
    GetGuildListResponse getGuildList();
    List<GetMyGuildResponse> myGuildList(String userId);
    List<SearchGuildListResponse> searchGuildList(String regionName, String gender);
}
