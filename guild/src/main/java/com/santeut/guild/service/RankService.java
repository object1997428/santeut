package com.santeut.guild.service;

import com.santeut.guild.feign.dto.PartyMemberInfoResponse;

public interface RankService {
    public PartyMemberInfoResponse getRankList(int loginUserId, int partyId, char type);
}
