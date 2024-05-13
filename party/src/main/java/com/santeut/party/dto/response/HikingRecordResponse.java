package com.santeut.party.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HikingRecordResponse {

  public List<HikingRecord> recordList;

  @Data
  @AllArgsConstructor
  @Builder
  public static class HikingRecord {

    public int partyUserId;
    public String partyName;
    public String guildName;
    public String mountainName;
    public String schedule;  // yyyy-MM-dd
    public Double distance;  // 이동거리(km)  cm -> km
    public Integer height;   // 최고고도(m)
    public Integer duration; // 이동시간(분)

    public static HikingRecord of(int partyUserId, String partyName, String guildName,
        String mountainName, LocalDateTime schedule,
        Integer distance, Integer height, Integer duration) {

      Double distanceInKm = null;
      if(distance != null) {
        distanceInKm = Math.round(((distance * 1.0) / 100000)*10)/10.0;
      }

      return HikingRecord.builder()
          .partyUserId(partyUserId)
          .partyName(partyName)
          .guildName(guildName)
          .mountainName(mountainName)
          .schedule(schedule.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .distance(distanceInKm)
          .height(height)
          .duration(duration)
          .build();
    }
  }
}
