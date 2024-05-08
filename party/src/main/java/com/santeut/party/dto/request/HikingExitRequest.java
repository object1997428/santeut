package com.santeut.party.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HikingExitRequest {
    int partyId;
//    int userId;
    int distance;
    int bestHeight;
    int moveTime;
    LocalDateTime endTime;
}
