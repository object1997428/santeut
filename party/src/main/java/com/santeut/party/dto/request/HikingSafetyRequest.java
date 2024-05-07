package com.santeut.party.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HikingSafetyRequest {
    String type;
    int partyId;
//    int userId;
    int distance;
    int bestHeight;
    LocalDateTime createdAt;
}
