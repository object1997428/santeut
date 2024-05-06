package com.santeut.guild.dto.request;

import lombok.Getter;

@Getter
public class CreateGuildRequest {

    String guildName;
    String guildInfo;
    boolean guildIsPrivate;
    int guildMember;
    int regionId;
    char guildGender;
    int guildMinAge;
    int guildMaxAge;
}
