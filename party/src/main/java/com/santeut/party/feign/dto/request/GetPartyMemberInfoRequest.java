package com.santeut.party.feign.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPartyMemberInfoRequest {

  int partyId;
  List<Integer> userIdList;

}
