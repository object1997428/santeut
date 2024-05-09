package com.santeut.party.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.santeut.party.entity.Party;
import lombok.Getter;

@Getter
public class PartyWithPartyUserIdResponse {

  private Party party;
  private int partyUserId;

  @QueryProjection
  public PartyWithPartyUserIdResponse(Party party, int partyUserId) {
    this.party = party;
    this.partyUserId = partyUserId;
  }
}
