package com.santeut.hiking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageLocationDto {
    private String type;
    private Integer partyId;
    private Integer userId;
    private Double lat;
    private Double rat;
}
