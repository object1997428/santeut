package com.santeut.party.dto.request;

import lombok.Getter;

@Getter
public class ModifyPartyRequestDto {

  public String schedule;
  public String partyName;
  public String place;
  public int maxPeople;

}
