package com.santeut.party.feign.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class GetPartyMemberInfoResponse {

  List<PartyMemberInfo> partyMembers;

  @Data
  public static class PartyMemberInfo {
    private int userId;
    private String userNickname;
    private String userProfile;
  }

}
