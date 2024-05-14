package com.santeut.guild.service;

import com.santeut.guild.dto.response.RankMembersInfoResponse;
import com.santeut.guild.feign.dto.PartyMemberInfoResponse;

public interface RankService {
    public RankMembersInfoResponse getRankList(int loginUserId, int partyId, char type);
}
