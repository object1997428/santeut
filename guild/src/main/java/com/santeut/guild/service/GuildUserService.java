package com.santeut.guild.service;

import com.santeut.guild.dto.response.ApplyGuildListResponse;

import java.util.List;

public interface GuildUserService {

    void applyGuild(int guildId, String userId);
    List<ApplyGuildListResponse> applyGuildList(int guildId);
}
