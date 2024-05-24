package com.santeut.guild.dto.response;

import com.santeut.guild.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetGuildListResponse {
    List<GetDetailGuildResponse> guildList = new ArrayList<>();
}
