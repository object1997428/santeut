package com.santeut.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuildPostFeignDto {
    private int userId;
    private String userNickname;
}
