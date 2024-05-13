package com.santeut.party.feign.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GuildInfoFeignResponseDto {

  int guildId;
  String guildName;
  String guildProfile;
  String guildInfo;
  int guildMember;
  int regionId;
  char guildGender;
  int guildMinAge;
  int guildMaxAge;
  LocalDateTime createdAt;

}
