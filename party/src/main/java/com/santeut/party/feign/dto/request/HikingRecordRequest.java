package com.santeut.party.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder @AllArgsConstructor
public class HikingRecordRequest {
    private int userId;
    private int distance;
    private int moveTime;
    private Boolean isFirstMountain;
}