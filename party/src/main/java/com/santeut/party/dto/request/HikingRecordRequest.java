package com.santeut.party.dto.request;

import jakarta.persistence.SqlResultSetMapping;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HikingRecordRequest {

  public int partyUserId;
  public String partyName;
  public int guildId;
  public String mountainName;
  public LocalDateTime schedule;
  public Integer distance;
  public Integer bestHeight;
  public Integer moveTime;

}
