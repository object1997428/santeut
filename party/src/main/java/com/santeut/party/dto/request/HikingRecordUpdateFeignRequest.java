package com.santeut.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor
public class HikingRecordUpdateFeignRequest {
    private int userId;
    private int distance;
    private int bestHeight;
    private int moveTime;
    private boolean isFirstMountain;
}