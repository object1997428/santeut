package com.santeut.hiking.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequestMessage {
    private String type;
    private Integer partyId;
    private Integer userId;
    private String userNickname;
    private Double lat;
    private Double lng;

    public static LocationRequestMessage fromRequestDto(LocationResponseMessage requestDto, Integer userId, String userNickname) {
        return LocationRequestMessage.builder()
                .type(requestDto.getType())
                .partyId(requestDto.getPartyId())
                .userId(userId)
                .userNickname(userNickname)
                .lat(requestDto.getLat())
                .lng(requestDto.getLng())
                .build();
    }
}
