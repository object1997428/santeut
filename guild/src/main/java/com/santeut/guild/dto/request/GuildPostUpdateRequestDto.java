package com.santeut.guild.dto.request;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildPostUpdateRequestDto {
    private String guildPostTitle;
    private String guildPostContent;
}
