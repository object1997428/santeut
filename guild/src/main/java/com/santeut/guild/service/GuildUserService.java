package com.santeut.guild.service;

import com.santeut.guild.dto.response.ApplyGuildListResponse;
import com.santeut.guild.dto.response.GuildMemberListResponse;

public interface GuildUserService {

    void applyGuild(int guildId, String userId);
    ApplyGuildListResponse applyGuildList(int guildId, String nowUserId);
    void approveApply(int guildId, int userId, String leaderUserId);
    void denyApply(int guildId, int userId, String leaderUserId);
    GuildMemberListResponse memberList(int guildId);
    void kickMember(int guildId, int userId, String leaderUserId);
    void delegateLeader(int guildId, int newLeaderId, int oldLeaderId);
    void quitGuild(int guildId, int userId);

}
