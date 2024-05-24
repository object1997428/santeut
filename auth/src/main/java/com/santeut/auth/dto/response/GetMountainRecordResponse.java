package com.santeut.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMountainRecordResponse {

    private int userDistance;
    private int userMoveTime;
    private int userHikingCount;
    private int userHikingMountain;
}
