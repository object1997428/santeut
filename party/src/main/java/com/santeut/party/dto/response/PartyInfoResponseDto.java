package com.santeut.party.dto.response;

import com.santeut.party.entity.Party;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartyInfoResponseDto {

  public int partyId;
  public String partyName;
  public String schedule;
  public String mountainName;
  public String place;
  public int maxPeople;
  public int curPeople;
  public String writer;
  public char status;

  public static PartyInfoResponseDto of(String nickname, Party entity) {
    return PartyInfoResponseDto.builder()
        .partyId(entity.getPartyId())
        .partyName(entity.getPartyName())
        .schedule(entity.getSchedule().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        .mountainName(entity.getMountainName())
        .place(entity.getPlace())
        .maxPeople(entity.getMaxParticipants())
        .curPeople(entity.getParticipants())
        .writer(nickname)
        .status(entity.getStatus())
        .build();
  }
}
