package com.santeut.guild.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class PatchGuildInfoRequest {

    String guildName;
    String guildInfo;
    boolean guildIsPrivate;
    Integer guildMember;
    Integer regionId;
    Character guildGender;
    Integer guildMinAge;
    Integer guildMaxAge;
}
