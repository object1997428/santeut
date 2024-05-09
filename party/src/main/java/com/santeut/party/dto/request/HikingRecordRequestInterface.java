package com.santeut.party.dto.request;

import java.time.LocalDateTime;

public interface HikingRecordRequestInterface {

  Integer getPartyUserId();
  String getPartyName();
  Integer getGuildId();
  String getMountainName();
  LocalDateTime getSchedule();
  Integer getDistance();
  Integer getBestHeight();
  Integer getMoveTime();

}
