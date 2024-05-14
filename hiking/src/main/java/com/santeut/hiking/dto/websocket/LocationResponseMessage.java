package com.santeut.hiking.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponseMessage {
    private String type;
    private Integer userId;
    private String userNickname;
    private Double lat;
    private Double lng;

    public static LocationResponseMessage fromRequestDto(LocationRequestMessage requestDto, Integer userId, String userNickname) {
        return LocationResponseMessage.builder()
                .type(requestDto.getType())
                .userId(userId)
                .userNickname(userNickname)
                .lat(requestDto.getLat())
                .lng(requestDto.getLng())
                .build();
    }
}
